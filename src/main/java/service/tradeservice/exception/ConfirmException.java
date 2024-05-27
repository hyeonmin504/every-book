package service.tradeservice.exception;

public class ConfirmException extends RuntimeException{
    public ConfirmException() {
    }

    public ConfirmException(String message) {
        super(message);
    }

    public ConfirmException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfirmException(Throwable cause) {
        super(cause);
    }

    public ConfirmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
