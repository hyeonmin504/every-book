package service.tradeservice.domain.item;

import jakarta.persistence.*;
import service.tradeservice.domain.item.Category;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;
    private Category category;
    private String itemName;
    private int price;
    private int stockQuantity;
    //private byte image;
    private LocalDateTime registerDate;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
}
