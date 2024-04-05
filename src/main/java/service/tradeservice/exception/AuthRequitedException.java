package service.tradeservice.exception;

public class AuthRequitedException extends RuntimeException{
    public AuthRequitedException() {
    }

    public AuthRequitedException(String message) {
        super(message);
    }

    public AuthRequitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthRequitedException(Throwable cause) {
        super(cause);
    }

    public AuthRequitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
