package service.tradeservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.tradeservice.domain.item.Item;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    public static final int TRADING = 1;
    public static final int SELL_CONFIRM = 2;
    public static final int TRADE_COMP = 3;
    public static final int TRADE_CANCEL = 0;

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    private int price;

    private int quantity;

    /**
     * 1 == 거래 전 - item.registerItem = SALE ( 대화방 생성 ) // 둘다 대화방 삭제 가능
     * 2 == 거래 중 - item.registerItem = NO_STOCK ( 판매자만 판매 결정을 확정 했을 경우 && 재고 수량이 없을 경우 ) // 구매자만 대화방 삭제 가능
     * 3 == 판매 완료 - item.registerItem = COMP ( 판매자, 구매자 둘다 확정을 결정한 경우 ) // 둘다 대화방 삭제 가능
     * 0 == 거래 실패 - item.registerItem = CANCEL ( 구매자가 구매 확정을 취소한 경우 ) // 둘다 대화방 삭제 가능
     */
    private int orderStatus;

    public void changeOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    private LocalDateTime soldDate;

    public Orders(int price, int quantity, int orderStatus) {
        this.price = price;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
    }

    public static Orders createOrder(int price, int quantity) {
        return new Orders(price, quantity, 1);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void soldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }
}
