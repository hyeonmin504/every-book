package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.tradeservice.domain.Orders;
import service.tradeservice.repository.OrderRepository;
import service.tradeservice.repository.impl.OrderRepositoryCustom;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    OrderRepository orderRepository;


}
