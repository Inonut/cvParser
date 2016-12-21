package cv.exception;

/**
 * Created by Dragos on 12/21/2016.
 */
public class ExtractionException extends RuntimeException {
    public ExtractionException() {
    }

    public ExtractionException(String message) {
        super(message);
    }

    public ExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractionException(Throwable cause) {
        super(cause);
    }

    public ExtractionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
