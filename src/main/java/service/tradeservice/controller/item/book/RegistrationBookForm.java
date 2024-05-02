package service.tradeservice.controller.item.book;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import service.tradeservice.domain.item.Category;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationBookForm {
    /**
     * 책을 등록할 때 사용하는 폼
     */

    @NotEmpty
    private String itemName;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    @Min(1)
    @Max(30)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String isbn;

    @NotEmpty
    private String author;

    @NotEmpty
    private String publisher;

    @NotEmpty
    private String publisherDate;

    /**
     * bookStatus, writtenStatus 기준
     * 0 최하 - 한 페이지 이상이 전체가 찢어짐 AND 필기, 낙서로 인해 글씨가 읽히지 않음, 이름이 적힘
     * 1 중하 - 글씨나 그림 부분이 조금 찢어짐 AND 그림, 글에 필기,낙서가 조금 있음, 이름이 적힘
     * 2 중 - 찢어진 부분이x 조금 있으나 뜻을 이해 하는데 지장이 없음 AND 필기가 있으나 낙서가 없음, 이름이 적히지 않음
     * 3 중상 - 찢어진 부분이 없으나 구겨짐 AND 글이나 그림에 지장 안가는 선에서 필기가 조금 있음, 이름이 적히지 않음
     * 4 최상 - 구겨짐, 찢어짐 없음 AND 낙서가 없음
     */
    @NotNull
    @Min(0)
    @Max(4)
    private int bookStatus;
    @NotNull
    @Min(0)
    @Max(4)
    private int writtenStatus;

    public RegistrationBookForm() {
    }

    public RegistrationBookForm(String itemName, int price, int stockQuantity, Category category, String author, String publisher, String publisherDate, int bookStatus, int writtenStatus) {
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
    }

    public static List<Category> Category() {
        List<Category> cate = new ArrayList<>();
        cate.add(Category.BOOK);
        return cate;
    }

    public static List<Integer> BookStatus() {
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(2);
        status.add(3);
        status.add(4);
        status.add(5);
        return status;
    }
    public static List<Integer> WrittenStatus() {
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(2);
        status.add(3);
        status.add(4);
        status.add(5);
        return status;
    }
}
