package trumps.Exceptions;

public class NotYourTurnException extends Exception {
    public NotYourTurnException() { super(); }
    public NotYourTurnException(String message) { super(message); }
    public NotYourTurnException(String message, Throwable t) { super(message, t); }
}
