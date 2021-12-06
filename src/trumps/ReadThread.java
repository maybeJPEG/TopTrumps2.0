package trumps;

import java.io.InputStream;

public class ReadThread {
    private final ReadThreadListener listener;
    private final InputStream is;

    public ReadThread(InputStream is, ReadThreadListener listener) {
        this.is = is;
        this.listener = listener;
    }
}
