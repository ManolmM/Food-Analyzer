package exceptions;

public class NoCommandProvidedException extends Exception {

    public NoCommandProvidedException(String message) {
        super(message);
    }

    public NoCommandProvidedException(String message, Throwable cause) {
        super(message, cause);
    }
}
