package trumps.Exceptions;

public class StartNotAllowedException extends Throwable {
    public StartNotAllowedException() { super(); }
    public StartNotAllowedException(String message) { super(message); }
    public StartNotAllowedException(String message, Throwable t) { super(message, t); }
}
