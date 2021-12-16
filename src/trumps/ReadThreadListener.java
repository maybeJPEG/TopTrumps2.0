package trumps;

public interface ReadThreadListener {
    byte INCREMENT_MESSAGE = 41;
    byte DECREMENT_MESSAGE = 42;

    /**
     * Tell listener message received
     * @param message
     */
    void recognizedMessage(byte message);

    /**
     * Tell listener: connection closed
     */
    void connectionClosed();
}
