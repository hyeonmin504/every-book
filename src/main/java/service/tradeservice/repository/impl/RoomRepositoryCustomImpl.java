package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;

import java.util.List;

@Repository
@Transactional
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom{

    @Autowired
    EntityManager em;

    @Override
    public void validationSellerId(Long sellerId) {

    }

    @Override
    public List<Room> findSameRoom(Long userId, Long itemId, int orderCount) {
        return em.createQuery("select r from Room r " +
                        "join fetch r.user u " +
                        "join fetch r.item i " +
                        "where u.id=:userId " +
                        "AND i.id=:itemId " +
                        "AND i.stockQuantity=:orderCount", Room.class)
                .setParameter("userId", userId)
                .setParameter("itemId", itemId)
                .setParameter("orderCount", orderCount)
                .getResultList();
    }
}
