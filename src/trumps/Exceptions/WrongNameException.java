package trumps.Exceptions;

public class WrongNameException extends Throwable {
    public WrongNameException() { super(); }
    public WrongNameException(String message) { super(message); }
    public WrongNameException(String message, Throwable t) { super(message, t); }
}
