package trumps;

import trumps.Exceptions.*;
import trumps.Impl.TopTrumpsImpl;
import trumps.tcp.*;

import java.io.*;

public class DistributedTTImpl extends TopTrumpsImpl implements ReadThreadListener, TCPCreatedListener {

            private static final byte CMD_START = 10;
            private static final byte CMD_FIRSTCARD = 11;
            private static final byte CMD_COMPARECATEGORY = 12;
            private InputStream is;
            private OutputStream os;

            public DistributedTTImpl() throws NotExistentValueException, NoCardsException {}

            @Override
            public int start() throws NotExistentPlayerException, StartNotAllowedException {
                return this.start(true);
            }

            @Override
            public int[] getFirstCard(int player) throws StatusException, NotExistentPlayerException, IOException {
                return this.getFirstCard(player, true);
            }

            @Override
            public int compareCategory(int category, int player) throws StatusException, NotExistentPlayerException, NotYourTurnException, CategoryDoesNotExistException, DrawException, IOException {
                return this.compareCategory(category, player, true);
            }

            @Override
            public void streamCreated(TCPStream channel) {
                try {
                    synchronized(this) {
                        this.is = channel.getInputStream();
                        this.os = channel.getOutputStream();
                    }
                    // run ReadThread
                    new ReadThread(this.is, this).run();
                } catch (IOException e) {
                    System.err.println("fatal: " + e.getLocalizedMessage());
                    System.exit(1);
                }
            }

            // guard method
            private boolean connected() {
                synchronized(this) {
                    if (this.is != null && this.os != null) return true;

                    // else
                    System.out.println("no yet connected");
                    return false;
                }
            }

            private int start(boolean localCall) throws NotExistentPlayerException, StartNotAllowedException {
                int player = 0;
                if (this.connected()) {
                    player = super.start();
                    if (localCall) {
                        DataOutputStream daos = new DataOutputStream(os);
                        try {
                            daos.write(CMD_START);
                            daos.writeInt(player);
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }
                return player;
            }

            private int[] getFirstCard(int player, boolean localCall) throws IOException, StatusException, NotExistentPlayerException {
                int[] retVal = null;
                if (this.connected()) {
                    retVal = super.getFirstCard(player);
                    if (localCall) {
                        DataOutputStream daos = new DataOutputStream(os);
                        try {
                            daos.write(CMD_FIRSTCARD);
                            daos.writeInt(player);
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }
                return retVal;
            }

            private int compareCategory(int category, int player, boolean localCall) throws IOException, StatusException, NotExistentPlayerException, NotYourTurnException, CategoryDoesNotExistException, DrawException  {
                int retPlayer = 0;
                if (this.connected()) {
                    retPlayer = super.compareCategory(category, player);
                    if (localCall) {
                        DataOutputStream daos = new DataOutputStream(os);
                        try {
                            daos.write(CMD_COMPARECATEGORY);
                            daos.writeInt(category);
                            daos.writeInt(player);
                        } catch (IOException e) {e.printStackTrace();}
                    }
                }
                return retPlayer;
            }

            @Override
            public void recognizedMessage(byte message) throws NotExistentPlayerException, StartNotAllowedException, StatusException, IOException {
                switch(message){
                    case CMD_START -> {
                        this.start(false);
 //TODO                       System.out.println("Game has started. It's Player, with PlayerID "" turn.");
                        break;
                    }
                    case CMD_FIRSTCARD -> {
                        this.getFirstCard(getActive_Player(), false);
                        System.out.println("First Card has been drawn");
                        break;
                    }
                    case CMD_COMPARECATEGORY -> {
//TODO                        this.compareCategory(?)
                        System.out.println("Comparing Category's.");
                        break;
                    }
                }

            }

            @Override
            public void connectionClosed() {
                this.is = null; this.os = null;
            }
        }
