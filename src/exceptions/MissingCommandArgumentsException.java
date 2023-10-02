package exceptions;

public class MissingCommandArgumentsException extends Exception {
    public MissingCommandArgumentsException(String message) {
        super(message);
    }

    public MissingCommandArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
