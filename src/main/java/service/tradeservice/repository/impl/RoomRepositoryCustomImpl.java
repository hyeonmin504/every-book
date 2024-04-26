package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Room> findByUser(Long userId) {
        return em.createQuery(
                        "SELECT r FROM Room r " +
                                "JOIN FETCH r.user u " + // 방과 사용자들을 조인합니다.
                                "JOIN FETCH r.contents c " + // 방과 콘텐츠들을 조인합니다.
                                "WHERE u.id = :userId " + // 사용자 ID가 주어진 ID와 일치하는지 확인합니다.
                                "AND c.sendDate = " + // 방의 마지막으로 저장된 콘텐츠의 LocalDateTime
                                "(SELECT MAX(c2.sendDate) FROM Content c2 WHERE c2.room = r)", Room.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
