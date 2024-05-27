package service.tradeservice.controller.trade;

import lombok.Data;

@Data
public class ErrorForm {
    private int errorCode;
    private String errorMessage;

    public ErrorForm(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
