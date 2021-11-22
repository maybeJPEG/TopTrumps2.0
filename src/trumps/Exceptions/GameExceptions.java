package trumps.Exceptions;

public class GameExceptions extends Exception {
    public GameExceptions() { super(); }
    public GameExceptions(String message) { super(message); }
    public GameExceptions(String message, Throwable t) { super(message, t); }
}
