package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Orders;
import service.tradeservice.repository.impl.OrderRepositoryCustom;

public interface OrderRepository extends JpaRepository<Orders,Long>, OrderRepositoryCustom {

}
