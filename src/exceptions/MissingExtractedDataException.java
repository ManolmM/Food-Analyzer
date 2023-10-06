package exceptions;

public class MissingExtractedDataException extends Exception {
    public MissingExtractedDataException(String message) {
        super(message);
    }

    public MissingExtractedDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
