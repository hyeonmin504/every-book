package service.tradeservice.controller.room;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ReceiveChatForm {
    @NotEmpty
    private String data;
}
