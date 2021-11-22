package trumps.Exceptions;

public class DrawException extends Exception{
    public DrawException() { super(); }
    public DrawException(String message) { super(message); }
    public DrawException(String message, Throwable t) { super(message, t); }
}
