package service.tradeservice.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import service.tradeservice.domain.item.Item;

import java.time.LocalDateTime;

@Entity
public class Book extends Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String isbn;
    private String witter;
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
}
