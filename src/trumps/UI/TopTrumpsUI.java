package trumps.UI;

import trumps.Exceptions.*;
import trumps.Impl.TopTrumpsImpl;

import java.io.*;
import java.util.*;

public class TopTrumpsUI {
    public static final String EXIT = "exit";
    public static final String DECKS = "decks";
    public static final String CARD = "card";

    private PrintStream standardOut = System.out;
    private PrintStream standardError = System.err;

    private BufferedReader userInput;
    private TopTrumpsImpl tt;

    public TopTrumpsImpl getTopTrumps() throws NotExistentValueException, NoCardsException {
        return new TopTrumpsImpl();
    }

    public static void main(String[] args) throws IOException {
        PrintStream os = System.out;

        os.println("TopTrumps, the Virtual Luck Version. Starting now...");
        TopTrumpsUI userCmd = new TopTrumpsUI(os, System.in);

        userCmd.printUsage();
        userCmd.runCommandLoop();
    }

    public TopTrumpsUI(PrintStream os, InputStream is) throws IOException {
        this.standardOut = os;
        this.userInput = is != null ? new BufferedReader(new InputStreamReader(is)) : null;

    }
    public void printUsage() {
        StringBuilder b = new StringBuilder();

        b.append("\n");
        b.append("\n");
        b.append("valid commands:");
        b.append("\n");
        b.append(EXIT);
        b.append(".. exit");
        b.append("\n");
        b.append(DECKS);
        b.append(".. will distribute the decks.");
        b.append("\n");
        b.append(CARD);
        b.append(".. will show your first Card of the deck.");
        b.append("\n");

        this.standardOut.println(b);
    }

    public void printUsage(String cmdString, String comment) {
        PrintStream out = this.standardOut;

        if(comment == null) comment = " ";
        out.println("malformed command: " + comment);
        out.println("use:");
        switch(cmdString) {
            case EXIT:
                out.println(EXIT + " channel name");
                out.println("example: " + EXIT + " localhost:7070");
                out.println("kills channel named localhost:7070");
                out.println("channel names are produced by using list");
                out.println(EXIT + " all .. kills all open connections");
                break;
        }
    }
    private List<String> cmds = new ArrayList<>();

    public void runCommandLoop() {
        boolean again = true;

        while(again) {
            boolean rememberCommand = true;
            String cmdLineString = null;

            try {
                // read user input
                cmdLineString = userInput.readLine();

                // finish that loop if less than nothing came in
                if(cmdLineString == null) break;

                // trim whitespaces on both sides
                cmdLineString = cmdLineString.trim();

                // extract command
                int spaceIndex = cmdLineString.indexOf(' ');
                spaceIndex = spaceIndex != -1 ? spaceIndex : cmdLineString.length();

                // got command string
                String commandString = cmdLineString.substring(0, spaceIndex);

                // extract parameters string - can be empty
                String parameterString = cmdLineString.substring(spaceIndex);
                parameterString = parameterString.trim();

                // start command loop
                switch(commandString) {
                    case DECKS:
                        this.distributingCards(); break;
                    case CARD:
                        this.getFirstCard(parameterString); break;
                    case EXIT:
                        this.exitGame("all");
                        again = false; break; // end loop

                    default: this.standardError.println("unknown command:" + cmdLineString);
                        this.printUsage();
                        rememberCommand = false;
                        break;
                }
            } catch (IOException | InterruptedException ex) {
                this.standardOut.println("cannot read from input stream");
                System.exit(0);
            } catch (StatusException e) {
                e.printStackTrace();
            } catch (GameExceptions gameExceptions) {
                gameExceptions.printStackTrace();
            } catch (WrongNameException e) {
                e.printStackTrace();
            } catch (NotExistentPlayerException e) {
                e.printStackTrace();
            } catch (NoCardsException e) {
                e.printStackTrace();
            } catch (NotExistentValueException e) {
                e.printStackTrace();
            } catch (StartNotAllowedException e) {
                e.printStackTrace();
            }

            if(rememberCommand) {
                this.cmds.add(cmdLineString);
            }
        }
    }

    private void distributingCards() throws StatusException, GameExceptions, WrongNameException, NotExistentPlayerException, StartNotAllowedException, NoCardsException, NotExistentValueException {
        this.tt = new TopTrumpsImpl();
        tt.start();
    }
    public void getFirstCard(String Player) throws StatusException, NotExistentPlayerException {
        StringTokenizer st = new StringTokenizer(Player);

        try {
            String playerString = st.nextToken();

            int playerNumber = Integer.parseInt(playerString);
            int[] cardCategories = this.tt.getFirstCard(playerNumber);
            if(playerNumber == 1)
                System.out.println("Alice's card: first value: " + cardCategories[0] + " second value: "+ cardCategories[1]+" third value: "+cardCategories[2]+" forth value: "+cardCategories[3]);
            else
                System.out.println("Bob's Card: first value: " + cardCategories[0] + " second value: "+ cardCategories[1]+" third value: "+cardCategories[2]+" forth value: "+cardCategories[3]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (NotExistentPlayerException e) {
            System.out.println("Player doesn't exist!");
        } catch (NoSuchElementException e){
            System.out.println("Please enter Player Number. Correct input: 'Card 1' or 'Card 2'");
        }
    }

    public void exitGame(String parameterString) throws InterruptedException {
        System.out.println("Exiting the Game! Goodbye");
        Thread.sleep(3000);
        System.exit(1);
    }

}
