package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import service.tradeservice.domain.Content;

@Repository
public class ContentRepositoryCustomImpl implements ContentRepositoryCustom{
    @Autowired
    EntityManager em;

    @Override
    public Content findSendInfo(Long userId, Long roomId) {
        return em.createQuery("select c from content c " +
                "join fetch c.room r " +
                "where c.sendUser=:userId AND " +
                "r.id=:roomId")
                .getSingleResult()
    }
}
