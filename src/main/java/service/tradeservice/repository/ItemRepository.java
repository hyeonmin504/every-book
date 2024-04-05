package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.item.Item;
import service.tradeservice.repository.impl.ItemRepositoryCustom;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
}
