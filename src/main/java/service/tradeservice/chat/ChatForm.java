package service.tradeservice.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatForm {

    @NotNull
    private Long sender;
    @NotEmpty
    private String message;
    @NotEmpty
    private String time;
    @NotNull
    private Long roomId;

    public ChatForm(Long sender, String message, String time, Long roomId) {
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.roomId = roomId;
    }
}
