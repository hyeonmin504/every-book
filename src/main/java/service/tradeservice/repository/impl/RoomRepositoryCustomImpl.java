package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;

import java.util.List;

@Repository
@Slf4j
@Transactional
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom{

    @Autowired
    EntityManager em;

    @Override
    public List<Room> findSameRoom(User user, Item item, int orderCount) {
        log.info("findSameRoom");
        return em.createQuery("select r from Room r " +
                        "join fetch r.order o " +
                        "where r.user=:user " +
                        "AND r.item=:item " +
                        "AND o.stock=:orderCount", Room.class)
                .setParameter("user", user)
                .setParameter("item", item)
                .setParameter("orderCount", orderCount)
                .getResultList();
    }

    @Override
    public List<Room> findOrderByRoom() {
        return em.createQuery(
                "select r from Room r " +
                        "join fetch r.order o",Room.class)
                .getResultList();
    }

    @Override
    public List<Room> findByUser(Long userId) {
        return em.createQuery(
                        "select r from Room r " +
                                "join fetch r.user u " + // 방과 사용자들을 조인합니다.
                                "join fetch r.contents c " + // 방과 콘텐츠들을 조인합니다.
                                "where u.id = :userId " + // 사용자 ID가 주어진 ID와 일치하는지 확인합니다.
                                "and c.sendDate = " + // 방의 마지막으로 저장된 콘텐츠의 LocalDateTime
                                "(select max(c2.sendDate) from Content c2 where c2.room = r)", Room.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Room> findBySellerRoom(Long sellerId) {
        return em.createQuery(
                "select r from Room r " +
                        "join fetch r.item i " +
                        "join fetch r.contents c " +
                        "where i.sellerId=:sellerId " +
                        "and c.sendDate = " +
                        "(select max(c2.sendDate) from Content c2 where c2.room = r)", Room.class)
                .setParameter("sellerId", sellerId)
                .getResultList();
    }
}
