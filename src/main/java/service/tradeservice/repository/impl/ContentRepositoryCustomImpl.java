package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;

import java.util.List;

@Repository
public class ContentRepositoryCustomImpl implements ContentRepositoryCustom{
    @Autowired
    EntityManager em;

    @Override
    public List<Content> findSendChat(Long roomId, User buyer, Long sellerId) {
        return em.createQuery("select c from Content c " +
                "join fetch c.room r " +
                "join fetch r.item i " +
                "where r.id=:roomId " +
                "And r.user=:user " +
                "And i.sellerId=:sellerId " +
                "order by", Content.class)
                .setParameter("roomId", roomId)
                .setParameter("buyer", buyer)
                .setParameter("sellerId", sellerId)
                .getResultList();
    }

    @Override
    public List<Content> findByRoom(Room room) {
        return em.createQuery("select c from Content c " +
                "where c.room=:room",Content.class)
                .setParameter("room", room)
                .getResultList();
    }

    @Override
    public List<Content> findLastChatInfo(Room room, User user) {
        return em.createQuery("select c from Content c " +
                        "join fetch c.room r " +
                        "WHERE c.room =:room " +
                        "And r.user =:user", Content.class)
                .setParameter("room",room)
                .setParameter("user",user)
                .getResultList();
    }

    @Override
    public List<Content> findLastChatInfoSellerVer(Room room, Long sellerId) {
        return em.createQuery(
                "select c from Content c " +
                        "join fetch c.room r " +
                        "join fetch r.item i " +
                        "WHERE c.room =:room " +
                        "And i.sellerId =:sellerId", Content.class)
                .setParameter("room",room)
                .setParameter("sellerId",sellerId)
                .getResultList();
    }
}
