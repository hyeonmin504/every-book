package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Ddd;

import java.util.List;

public interface DataRepository extends JpaRepository<Ddd, Long> {
    long countById(Long id);
    List<Ddd> findDistinctByPrice(int price);
}
