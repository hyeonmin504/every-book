package service.tradeservice.controller.item;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import service.tradeservice.domain.item.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationItemForm {
    /**
     * 상품 정보를 간단하게 보여주는 폼
     */

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

    public RegistrationItemForm() {}

    public RegistrationItemForm(Long id,String itemName, int price, int stockQuantity, String nickName, Category category) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.nickName = nickName;
        this.category = category;
    }
}
