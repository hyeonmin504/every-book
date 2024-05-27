package service.tradeservice.controller.room;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.UserRepository;

import java.time.LocalDateTime;

@Data
public class
RoomListForm {
    /**
     * 채팅방 리스트에서 보이는 폼
     */

    @Autowired
    UserRepository userRepository;

    @NotNull
    private Long id;
    //content
    @NotNull
    private int stock;
    @NotEmpty
    private String sendDate;
    @NotEmpty
    private String content;

    //order
    @NotNull
    private String orderStatus;

    //item
    @NotEmpty
    private String itemName;
    @NotNull
    private int price;

    public RoomListForm() {
    }

    public RoomListForm(int orderStatus, Long id, int stock, String sendDate, String content, String itemName, int price) {
        transformStatus(orderStatus);
        setContentInfo(id, stock, sendDate, content);
        setItemInfo(itemName, price);
    }

    public void transformStatus(int status){
        if (status == 0){
            setOrderInfo("거래 실패");
        }
        if (status == 1){
            setOrderInfo("거래 전");
        }
        if (status == 2){
            setOrderInfo("거래 중");
        }
        if (status == 3){
            setOrderInfo("거래 완료");
        }
    }

    public void setOrderInfo(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setContentInfo(Long id, int stock, String sendDate, String content) {
        this.id = id;
        this.stock = stock;
        this.sendDate = sendDate;
        this.content = content;
    }

    public void setItemInfo(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }


}
