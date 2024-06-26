package service.tradeservice.domain.item;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.NotEnoughStockException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(name = "seller_id")
    private Long sellerId;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    private String itemName;
    private int price;
    private int stockQuantity;
    //private byte image;
    private String registerDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_status")
    private RegisterStatus registerStatus;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Favorite> favorite = new ArrayList<>();

    public void setRegisteredItemDate() {
        this.registerDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.registerStatus = service.tradeservice.domain.item.RegisterStatus.SALE;
    }

    public Boolean compareStockQuantity(Item item,int compareStock) {
        if (item.getStockQuantity() >= compareStock && compareStock > 0 ){
            return true;
        }
        return false;
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

    public Item(Category category, String itemName, int price, int stockQuantity) {
        this.category = category;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.registerDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.registerStatus = RegisterStatus.SALE;
    }

    public Item(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void updateItemInfo(Item item) {
        this.stockQuantity = item.stockQuantity;
        this.price = item.price;
        this.registerDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //this.image = item.image;
    }
}
