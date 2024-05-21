package service.tradeservice.exception;


public class DddException extends Exception{
    public DddException() {
    }

    public DddException(String message) {
        super(message);
    }

    public DddException(String message, Throwable cause) {
        super(message, cause);
    }

    public DddException(Throwable cause) {
        super(cause);
    }

    public DddException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
