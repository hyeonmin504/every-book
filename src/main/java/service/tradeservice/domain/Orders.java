package service.tradeservice.domain;

import jakarta.persistence.*;
import service.tradeservice.domain.item.Item;

import java.time.LocalDateTime;

@Entity
public class Orders {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "iem_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    private int price;
    private int quantity;

    /**
     * 0 == 거래 중 ( 대화방 생성 )
     * 1 == 거래 완료
     * 2 == 거래 실패
     */
    private int orderStatus;

    private LocalDateTime soldDate;
}
