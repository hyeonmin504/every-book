package service.tradeservice.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public enum Category {
    BOOK("책");

    private final String category;

    Category(String category) {
        this.category = category;
    }
}
