package exceptions;

public class EmptyCommandException extends Exception {

    public EmptyCommandException(String message) {
        super(message);
    }

    public EmptyCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
