package trumps;

import trumps.Exceptions.*;
import trumps.Impl.TopTrumpsImpl;
import trumps.tcp.TCPStream;

import java.io.*;

public class DistributedTTImpl extends TopTrumpsImpl
//        implements TCPStreamCreatedListener, ReadThreadListener
        {

            private static final byte CMD_FIRSTCARD = 1;
            private InputStream is;
            private OutputStream os;

            public DistributedTTImpl() throws NotExistentValueException, NoCardsException {}

            @Override
            public int start() throws NotExistentPlayerException, StartNotAllowedException {
                return this.start(true);
            }

            @Override
            public int[] getFirstCard(int player) throws StatusException, NotExistentPlayerException {
                return this.getFirstCard(player, true);
            }

            @Override
            public int compareCategory(int category, int player) throws StatusException, NotExistentPlayerException, NotYourTurnException, CategoryDoesNotExistException, DrawException {
                return super.compareCategory(category, player, true);
            }

            @Override
            public void streamCreated(TCPStream channel) {
                try {
                    synchronized(this) {
                        this.is = channel.getInputStream();
                        this.os = channel.getOutputStream();
                    }
                    // run ReadThread
                    new ReadThread(this.is, this).start();
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
                            daos.writeBytes("Game started");
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
        }
