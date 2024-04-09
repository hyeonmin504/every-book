package service.tradeservice.repository.impl;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    @Autowired
    EntityManager em;

}
