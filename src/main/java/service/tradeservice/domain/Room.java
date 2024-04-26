package service.tradeservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    private String createDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;


    @OneToOne(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Order order;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Content> contents = new ArrayList<>();

    /**
     * status
     * 0 == VISIBLE - TRADING, SELL_CONFIRM, TRADE_COMPㅎ
     * 1 == INVISIBLE - TRADE_CANCEL
     */
    public static final int VISIBLE = 0;
    public static final int INVISIBLE = 1;
    private int state;

    public void changeState(int state) {
        this.state = state;
    }
    protected Room(User user, Item item, Order orders) {
        this.state = VISIBLE;
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        setUser(user);
        setItem(item);
        setOrder(orders);
    }

    public static Room createRoom(User user, Item item, Order orders) {
        return new Room(user, item, orders);
    }

    // == 연관관계 매서드 == //
    private void setUser(User user){
        this.user = user;
        user.getRooms().add(this);
    }
    private void setItem(Item item){
        this.item = item;
        item.getRooms().add(this);
    }
    private void setOrder(Order order){
        this.order = order;
        order.setRoom(this);
    }

    private void setContent(Content content) {
        this.contents.add(content);
        content.setRoom(this);
    }
}
