package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;

import java.util.List;
import java.util.Optional;

public class FavoriteRepositoryCustomImpl implements  FavoriteRepositoryCustom{
    @Autowired
    EntityManager em;
    @Override
    public List<Favorite> findItemsByUserId(Long userId) {
        return em.createQuery("select f from Favorite f " +
                        "join fetch f.user u " +
                        "join fetch f.item i " +
                        "where u.id=:userId ", Favorite.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Favorite findByUserIdAndItemId(Long userId, Long itemId) {
        return em.createQuery("select f from Favorite f " +
                        "join fetch f.user u " +
                        "join fetch f.item i " +
                        "where u.id=:userId " +
                        "And i.id=:itemId", Favorite.class)
                .setParameter("userId", userId)
                .setParameter("itemId", itemId)
                .getSingleResult();
    }
}
