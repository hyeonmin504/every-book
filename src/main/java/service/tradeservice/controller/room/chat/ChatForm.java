package service.tradeservice.controller.room.chat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatForm {

    @NotNull
    private Long sendUser;
    @NotEmpty
    private String content;
    @NotEmpty
    private String time;
    @NotNull
    private Long roomId;

    public ChatForm(Long sendUser, String content, String time, Long roomId) {
        this.sendUser = sendUser;
        this.content = content;
        this.time = time;
        this.roomId = roomId;
    }
}
