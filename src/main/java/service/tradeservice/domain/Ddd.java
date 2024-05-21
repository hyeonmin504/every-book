package service.tradeservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Ddd {
    @Id
    @GeneratedValue
    private Long id;
    private int price;

    public Ddd() {
    }

    public Ddd(int price) {
        this.price = price;
    }
}
