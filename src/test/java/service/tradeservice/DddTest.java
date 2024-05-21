package service.tradeservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import service.tradeservice.domain.Ddd;
import service.tradeservice.exception.DddException;
import service.tradeservice.repository.DataRepository;
import service.tradeservice.service.DataService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class DddTest {
    @Autowired
    DataRepository dataRepository;
    @Autowired
    DataService dataService;

    @Test
    public void 예외_처리() {
        Ddd data = new Ddd(1);

        assertThatThrownBy(()->dataService.ex(data)).isInstanceOf(RuntimeException.class);

    }
    @Test
    @Commit
    public void 예외_처리2() {
        Ddd data = new Ddd(2);
        Ddd data2 = new Ddd(3);
        Ddd data3 = new Ddd(4);
        Ddd data4 = new Ddd(5);
        try {
            dataService.ex(data2);
            dataService.ex(data3);
            dataService.ex(data4);
            dataService.ex(data);
        } catch (DddException e) {
        }
        Ddd ddd = dataRepository.findById(data.getId()).orElseThrow();
        long l = dataRepository.countById(data.getId());
        System.out.println("l = " + l);

        Object distinctByPrice = dataRepository.findDistinctByPrice(3);
        System.out.println("distinctByPrice = " + distinctByPrice);

        assertThat(ddd.getPrice()).isEqualTo(0);
    }
}
