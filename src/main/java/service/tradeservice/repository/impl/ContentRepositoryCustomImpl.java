package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.tradeservice.domain.Content;

import java.util.List;

@Repository
public class ContentRepositoryCustomImpl implements ContentRepositoryCustom{
    @Autowired
    EntityManager em;

    @Override
    public List<Content> findSendChat(Long roomId) {
        return em.createQuery("select c from Content c " +
                "join fetch c.room r " +
                "where r.id=:roomId", Content.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }
}
