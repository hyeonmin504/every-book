package service.tradeservice.controller.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ChatForm {

    @NotNull
    private String sender;
    @NotEmpty
    private String message;
    @NotEmpty
    private String time;
    @NotNull
    private Long roomId;

    public ChatForm(String sender, String message, String time, Long roomId) {
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.roomId = roomId;
    }
}
