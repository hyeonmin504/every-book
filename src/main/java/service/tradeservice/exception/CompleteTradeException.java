package service.tradeservice.exception;

public class CompleteTradeException extends RuntimeException{
    public CompleteTradeException() {
    }

    public CompleteTradeException(String message) {
        super(message);
    }

    public CompleteTradeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompleteTradeException(Throwable cause) {
        super(cause);
    }

    public CompleteTradeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
