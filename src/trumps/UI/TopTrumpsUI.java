package trumps.UI;

import java.awt.desktop.ScreenSleepEvent;
import java.io.*;
import java.util.*;

public class TopTrumpsUI {
    public static final String CONNECT = "connect";
    public static final String OPEN = "open";
    public static final String Exit = "kill";

    private PrintStream standardOut = System.out;
    private PrintStream standardError = System.err;

    private BufferedReader userInput;

    public static void main(String[] args) throws IOException {
        PrintStream os = System.out;

        os.println("TopTrumps, the virtual luck version.");
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
        b.append(CONNECT);
        b.append(".. connect to remote engine");
        b.append("\n");
        b.append(OPEN);
        b.append(".. open socket");
        b.append("\n");
        b.append(Exit);
        b.append(".. exit");

        this.standardOut.println(b.toString());
    }

    public void printUsage(String cmdString, String comment) {
        PrintStream out = this.standardOut;

        if(comment == null) comment = " ";
        out.println("malformed command: " + comment);
        out.println("use:");
        switch(cmdString) {
            case CONNECT:
                out.println(CONNECT + " [IP/DNS-Name_remoteHost] remotePort localEngineName");
                out.println("omitting remote host: localhost is assumed");
                out.println("example: " + CONNECT + " localhost 7070 Bob");
                out.println("example: " + CONNECT + " 7070 Bob");
                out.println("in both cases try to connect to localhost:7070 and let engine Bob handle " +
                        "connection when established");
                break;
            case OPEN:
                out.println(OPEN + " localPort engineName");
                out.println("example: " + OPEN + " 7070 Alice");
                out.println("opens a server socket #7070 and let engine Alice handle connection when established");
                break;
            case Exit:
                out.println(Exit + " channel name");
                out.println("example: " + Exit + " localhost:7070");
                out.println("kills channel named localhost:7070");
                out.println("channel names are produced by using list");
                out.println(Exit + " all .. kills all open connections");
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
                switch(commandString) {/*
                    case CONNECT:
                        this.doConnect(parameterString); break;
                    case OPEN:
                        this.doOpen(parameterString); break;*/
                    case Exit:
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
            }

            if(rememberCommand) {
                this.cmds.add(cmdLineString);
            }
        }
    }
    /*public void doConnect(String parameterString){
        StringTokenizer st = new StringTokenizer(parameterString);

        try {
            String remoteHost = st.nextToken();
            String remotePortString = st.nextToken();
            String engineName = null;
            if(!st.hasMoreTokens()) {
                // no remote host set - shift
                engineName = remotePortString;
                remotePortString = remoteHost;
                remoteHost = "localhost";
            } else {
                engineName = st.nextToken();
            }
            int remotePort = Integer.parseInt(remotePortString);

            String name =  remoteHost + ":" + remotePortString;

            this.startTCPStream(name,  new TCPStream(remotePort, false, name), engineName);
        }
        catch(RuntimeException re) {
            this.printUsage(CONNECT, re.getLocalizedMessage());
        }
    }*/
/*
    public void doOpen(String parameterString) {
        StringTokenizer st = new StringTokenizer(parameterString);

        try {
            String portString = st.nextToken();
            String engineName = st.nextToken();

            int port = Integer.parseInt(portString);
            String name =  "server:" + port;

            this.startTCPStream(name,  new TCPStream(port, true, name), engineName);
        }
        catch(RuntimeException re) {
            this.printUsage(OPEN, re.getLocalizedMessage());
        }
    }*/

    public void exitGame(String parameterString) throws InterruptedException {
        System.out.println("Exiting the Game");
        Thread.sleep(3000);
        System.exit(1);
    }

}
