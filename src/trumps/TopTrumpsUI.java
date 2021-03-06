package trumps;

import trumps.Exceptions.*;
import trumps.Impl.TopTrumpsImpl;
import trumps.tcp.*;
import java.io.*;
import java.util.*;

public class TopTrumpsUI {
    public static final String EXIT = "exit";
    public static final String DECKS = "decks";
    public static final String WHO = "who";
    public static final String CARD = "card";
    public static final String COMPARE = "compare";

    private PrintStream standardOut = System.out;
    private PrintStream standardError = System.err;

    public static final int PORTNUMBER = 7777;

    private BufferedReader userInput;


    public static void main(String[] args) throws IOException, NoCardsException, NotExistentValueException {
        PrintStream os = System.out;

        os.println("TopTrumps, the Virtual Luck Version. Starting now...");
        DistributedTTImpl distrApp = new DistributedTTImpl();
        TCPStream tcpStream;
        if(args.length > 0) {
            System.out.println("init as TCP client");
            tcpStream = new TCPStream(PORTNUMBER, false, "Client", distrApp);
        } else {
            System.out.println("init as TCP server");
            tcpStream = new TCPStream(PORTNUMBER, true, "Server", distrApp);
        }

        // try to establish connection
        tcpStream.start();

        new TopTrumpsUI(distrApp).runCommandLoop();

        // command loop ended - kill it under all circumstances
        tcpStream.kill();

        System.out.println("connection closed");
    }

    private final TopTrumps tt;

    private TopTrumpsUI(TopTrumps tt) {
        this.tt = tt;
    }

    public TopTrumpsUI(PrintStream os, InputStream is, TopTrumps tt) throws IOException {
        this.standardOut = os;
        this.userInput = is != null ? new BufferedReader(new InputStreamReader(is)) : null;

        this.tt = tt;
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
        b.append(".. will distribute the decks. Start the Game with this command.");
        b.append("\n");
        b.append(WHO);
        b.append(".. will tell you who's turn it is.");
        b.append("\n");
        b.append(CARD);
        b.append(" <PlayerID> ( ID Alice = 1, ID Bob = 2 ) .. will show your first Card of the deck. Type in Card 1 to see Alice's Card or Card 2 if you want to see Bob's Card.");
        b.append("\n");
        b.append(COMPARE);
        b.append(" <PlayerID> <Category (1,2,3,4)>.. choose your Value and compare it to your opponent. Format example: 'compare 2 1'");
        b.append("\n");
        b.append("\n");
        b.append("Always start by typing in the command 'decks' in order to shuffle the deck and distribute the cards to each player.");


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
                    case WHO:
                        this.getActivePlayer(); break;
                    case DECKS:
                        this.distributingCards(); break;
                    case CARD:
                        this.getFirstCard(parameterString); break;
                    case COMPARE:
                        this.compareCategory(parameterString); break;
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
        tt.start();
    }

    private void getActivePlayer() throws NotExistentPlayerException {

        try {
            String PlayerName = null;
            int Player = this.tt.getActive_Player();
            if (Player == 1) PlayerName = "Alice";
            if (Player == 2) PlayerName = "Bob";

            System.out.println(PlayerName + " is next.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFirstCard(String Player) throws StatusException, NotExistentPlayerException {
        StringTokenizer st = new StringTokenizer(Player);

        try {
            String playerString = st.nextToken();

            int playerNumber = Integer.parseInt(playerString);
            int[] cardCategories = this.tt.getFirstCard(playerNumber);
            if (playerNumber != this.tt.getActive_Player()){
                throw new NotYourTurnException();
            }
            if(playerNumber == 1)
                System.out.println("Alice's card: first value: " + cardCategories[0] + ", second value: "+ cardCategories[1]+", third value: "+cardCategories[2]+", forth value: "+cardCategories[3]);
            if(playerNumber == 2)
                System.out.println("Bob's Card: first value: " + cardCategories[0] + ", second value: "+ cardCategories[1]+", third value: "+cardCategories[2]+", forth value: "+cardCategories[3]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (NotExistentPlayerException e) {
            System.out.println("Player doesn't exist!");
        } catch (NoSuchElementException e){
            System.out.println("Please enter Player Number. Correct input: 'Card 1' or 'Card 2'");
        } catch (NotYourTurnException e) {
            System.out.println("You can only ask to see the Card of the Player who's turn it is.");
        } catch (GameExceptions gameExceptions) {
            gameExceptions.printStackTrace();
        } catch (MatchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void compareCategory(String Parameterstring){
        StringTokenizer st = new StringTokenizer(Parameterstring);
        try {
            String stringValue = st.nextToken();
            String stringPlayer = st.nextToken();

            int Category = Integer.parseInt(stringValue);
            int playerNumber = Integer.parseInt(stringPlayer);
            int winner = this.tt.compareCategory(Category, playerNumber);
            if(winner == 1){
                System.out.println("Alice won! You are next. Draw a card and choose the next Value you want to play.");
            }
            if(winner == 2){
                System.out.println("Bob won! You are next. Draw a card and choose the next Value you want to play.");
            }
        } catch (StatusException e) {
            e.printStackTrace();
        } catch (NotExistentPlayerException e) {
            System.out.println("Player doesn't exist!");
        } catch (NotYourTurnException e) {
            System.out.println("Not your turn. Ask who is next and play that players card.");
        } catch (CategoryDoesNotExistException e) {
            System.out.println("This Category does not exist! Type in the correct value.");
        } catch (DrawException e) {
            System.out.println("DRAW! Cards are saved. Take a new Card and compare the values. The winner of the next Round will get all the Cards of the played rounds. ");
        } catch (GameExceptions gameExceptions) {
            gameExceptions.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void exitGame(String parameterString) throws InterruptedException {
        System.out.println("Exiting the Game! Goodbye");
        Thread.sleep(3000);
        System.exit(1);
    }

}
