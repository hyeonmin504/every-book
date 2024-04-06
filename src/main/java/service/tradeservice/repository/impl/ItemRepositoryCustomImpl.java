package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;

import java.util.List;

@Repository
@Transactional
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    @Autowired
    EntityManager em;

    /**
     * 현재 orders.orderStatus = sell_confirm인 경우가 있으면 해당 room만 찾아서 반환
     * @param item
     * @return
     */
    @Override
    public List<Room> findRoom(Item item) {
        return em.createQuery("select r from Item i " +
                "join i.rooms r " +
                "join r.order o " +
                "where o.orderStatus=2", Room.class)
                .getResultList();
    }
}
