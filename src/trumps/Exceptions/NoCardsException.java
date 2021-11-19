package trumps.Exceptions;

public class NoCardsException extends Exception{
    public NoCardsException() { super(); }
    public NoCardsException(String message) { super(message); }
    public NoCardsException(String message, Throwable t) { super(message, t); }
}
