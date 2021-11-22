package trumps.Exceptions;

public class MatchException extends Exception {
    public MatchException() { super(); }
    public MatchException(String message) { super(message); }
    public MatchException(String message, Throwable t) { super(message, t); }
}
