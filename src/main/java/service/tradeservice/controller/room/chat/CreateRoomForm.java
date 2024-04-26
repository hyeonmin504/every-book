package service.tradeservice.controller.room.chat;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateRoomForm {

    //order
    @NotNull
    private int stock;
    @NotNull
    private Long userId;
    @NotNull
    private Long itemId;
    @NotNull
    private Long orderId;

    public void createRoom(Long userId, Long itemId, Long orderId) {
        this.userId = userId;
        this.itemId = itemId;
        this.orderId = orderId;
    }
}
