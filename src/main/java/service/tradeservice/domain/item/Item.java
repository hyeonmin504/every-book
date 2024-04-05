package service.tradeservice.domain.item;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import service.tradeservice.exception.NotEnoughStockException;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

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

    public int removeStock(int quantity) {
        if(this.stockQuantity - quantity >= 0) {
            this.stockQuantity -= quantity;
            return stockQuantity;
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
