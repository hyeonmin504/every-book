package service.tradeservice.exception;

public class DuplicationUserException extends RuntimeException {
    public DuplicationUserException() {
    }

    public DuplicationUserException(String message) {
        super(message);
    }

    public DuplicationUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicationUserException(Throwable cause) {
        super(cause);
    }

    public DuplicationUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
