package service.tradeservice.controller.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.user.University;

@Data
public class BookInfoForm {

    @NotNull
    private Long id;
    @NotEmpty
    private String itemName;
    @NotNull
    private int price;
    @NotNull
    private int stockQuantity;
    @NotEmpty
    private String nickName;

    private Category category;
    @NotNull
    private int sellerSoldItemCount;

    private University sellerUniv;
    @NotNull
    private int writtenStatus;
    @NotNull
    private int bookStatus;

    public BookInfoForm() {
    }

    public BookInfoForm(Long id, String itemName, int price, int stockQuantity, String nickName, Category category, int sellerSoldItemCount, University university) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.nickName = nickName;
        this.category = category;
        this.sellerSoldItemCount = sellerSoldItemCount;
        this.sellerUniv = university;
    }
}
