package service.tradeservice.domain.item;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Book extends Item {
    private String isbn;
    private String author;
    private String publisher;
    private String publisherDate;

    /**
     * bookStatus, writtenStatus 기준
     * 0 최하 - 한 페이지 이상이 전체가 찢어짐 AND 필기, 낙서로 인해 글씨가 읽히지 않음, 이름이 적힘
     * 1 중하 - 글씨나 그림 부분이 조금 찢어짐 AND 그림, 글에 필기,낙서가 조금 있음, 이름이 적힘
     * 2 중 - 찢어진 부분이 조금 있으나 뜻을 이해 하는데 지장이 없음 AND 필기가 있으나 낙서가 없음, 이름이 적히지 않음
     * 3 중상 - 찢어진 부분이 없으나 구겨짐 AND 글이나 그림에 지장 안가는 선에서 필기가 조금 있음, 이름이 적히지 않음
     * 4 최상 - 구겨짐, 찢어짐 없음 AND 낙서가 없음
     */
    private int bookStatus;
    private int writtenStatus;

    public Book(Category category, String itemName, int price, int stockQuantity, String author, String publisher, String publisherDate, int bookStatus, int writtenStatus) {
        super(category, itemName, price, stockQuantity);
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
    }

    public Book(int stockQuantity,String author, String publisher, String publisherDate, int bookStatus, int writtenStatus) {
        super(stockQuantity);
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
    }

    public void createBook(String author, String publisher, String publisherDate, int bookStatus, int writtenStatus, Category category, String itemName, int price, int stockQuantity, RegisterStatus status) {
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
        setItem(category,itemName,price,stockQuantity, status);

    }

    protected void setBook(String author, String publisher, String publisherDate, int bookStatus, int writtenStatus) {
        this.author = author;
        this.publisher = publisher;
        this.publisherDate = publisherDate;
        this.bookStatus = bookStatus;
        this.writtenStatus = writtenStatus;
    }

    public static Book updateBook(Book book,Book findBook) {
        findBook.updateItemInfo(book);
        findBook.setBook(book.author,book.publisher,book.publisherDate,book.bookStatus,book.writtenStatus);

        return findBook;
    }

}
