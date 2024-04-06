package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.item.Item;

import java.util.List;

@Repository
@Transactional
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    @Autowired
    EntityManager em;

    @Override
    public List<Item> findRoom(Item item) {
        return em.createQuery("select i from Item i " +
                "join i.rooms r " +
                "where r.state=0", Item.class)
                .getResultList();
    }
}
