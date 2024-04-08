package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Orders;

@Repository
@Transactional
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    @Autowired
    EntityManager em;

}
