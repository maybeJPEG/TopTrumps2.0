package trumps;

import trumps.Exceptions.NotExistentPlayerException;
import trumps.Exceptions.StartNotAllowedException;
import trumps.Exceptions.StatusException;

import java.io.IOException;

public interface ReadThreadListener {
    byte INCREMENT_MESSAGE = 41;
    byte DECREMENT_MESSAGE = 42;

    /**
     * Tell listener message received
     * @param message
     */
    void recognizedMessage(byte message) throws NotExistentPlayerException, StartNotAllowedException, StatusException, IOException;

    /**
     * Tell listener: connection closed
     */
    void connectionClosed();
}
