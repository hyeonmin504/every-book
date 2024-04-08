package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Order;
import service.tradeservice.repository.impl.OrderRepositoryCustom;

public interface OrderRepository extends JpaRepository<Order,Long>, OrderRepositoryCustom {

}
