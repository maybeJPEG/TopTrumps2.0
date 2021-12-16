package trumps;

import java.io.IOException;
import java.io.InputStream;

public class ReadThread {
    private final ReadThreadListener listener;
    private final InputStream is;

    public ReadThread(InputStream is, ReadThreadListener listener) {
        this.is = is;
        this.listener = listener;
    }
    public void run() {
        try {
            while(true) {
                int readInt = this.is.read();
                byte messageByte = (byte) readInt; // remove first bytes
                this.listener.recognizedMessage(messageByte);
            }
        } catch (IOException e) {
            System.err.println("connection broken: " + e.getLocalizedMessage());
            this.listener.connectionClosed();
        }
    }
}
