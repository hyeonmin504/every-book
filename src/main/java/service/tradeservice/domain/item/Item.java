package service.tradeservice.domain.item;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import service.tradeservice.domain.Room;
import service.tradeservice.exception.NotEnoughStockException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Slf4j
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "ca")
    private Category category;

    private String itemName;
    private int price;
    private int stockQuantity;
    //private byte image;
    private LocalDateTime registerDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_status")
    private RegisterStatus registerStatus;

    public void setRegisteredItemDate() {
        this.registerDate = LocalDateTime.now();
        this.registerStatus = service.tradeservice.domain.item.RegisterStatus.SALE;
    }

    public void changeRegisterStatus(RegisterStatus registerStatus){
        this.registerStatus = registerStatus;
    }

    public void setSeller(Long seller) {
        this.sellerId = seller;
    }

    public void setItem(Category category, String itemName, int price, int stockQuantity, RegisterStatus registerStatus) {
        this.category = category;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.registerStatus = registerStatus;
    }

    public void removeStock(int quantity) {
        if(this.stockQuantity - quantity >= 0) {
            this.stockQuantity -= quantity;
            log.info("재고 감소 완료={}", stockQuantity);
        } else {
            throw new NotEnoughStockException("need more stock");
        }
    }

    public Item() {
    }

    public Item(Category category, String itemName, int price, int stockQuantity, LocalDateTime registerDate, RegisterStatus registerStatus) {
        this.category = category;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.registerDate = registerDate;
        this.registerStatus = registerStatus;
    }

    public void updateItemInfo(Item item) {
        this.itemName = item.itemName;
        this.price = item.price;
        this.stockQuantity = item.stockQuantity;
        //this.image = item.image;
    }
}
