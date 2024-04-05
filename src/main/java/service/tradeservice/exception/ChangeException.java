package service.tradeservice.exception;

public class ChangeException extends RuntimeException{
    public ChangeException() {
    }

    public ChangeException(String message) {
        super(message);
    }

    public ChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangeException(Throwable cause) {
        super(cause);
    }

    public ChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
