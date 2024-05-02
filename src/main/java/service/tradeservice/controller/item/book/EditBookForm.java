package service.tradeservice.controller.item.book;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.user.University;

@Data
public class EditBookForm {
    @NotNull
    @Min(1)
    @Max(30)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotEmpty
    private String author;

    @NotEmpty
    private String publisher;

    @NotEmpty
    private String publisherDate;

    @NotNull
    @Min(0)
    @Max(4)
    private int bookStatus;
    @NotNull
    @Min(0)
    @Max(4)
    private int writtenStatus;

    public EditBookForm() {
    }

    public EditBookForm(Integer stockQuantity, Category category, String author, String publisher, String publisherDate, int bookStatus, int writtenStatus) {
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
    }
}
