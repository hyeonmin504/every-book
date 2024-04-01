package service.tradeservice.domain;

import jakarta.persistence.*;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.SaleStatus;
import service.tradeservice.domain.user.User;

@Entity
public class Favorite {
    @Id @GeneratedValue
    @Column(name = "favorite_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
}
