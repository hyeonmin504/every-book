package service.tradeservice.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.controller.item.RegistrationItemForm;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;

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
                "where o.order" +
                        "Status=2", Room.class)
                .getResultList();
    }

    @Override
    public List<Item> findAllByUniv(University univ) {
        return em.createQuery("select i from Item i " +
                        "join fetch User u on i.sellerId = u.id " +
                        "where u.university = :univ", Item.class)
                .setParameter("univ", univ)
                .getResultList();
    }

    @Override
    public List<Item> findMyItem(Long userId) {
        return em.createQuery("select i from Item i " +
                "where i.sellerId=:userId", Item.class)
                .setParameter("userId", userId)
                .getResultList();
    }


//    @Override
//    public List<RegistrationItemForm> findItemAndSellerNickName() {
//        List resultList = em.createQuery("select i.itemName, i.price, i.stockQuantity, u.nickName from Item i,User u " +
//                        "join i.sellerId=u.id")
//                .getResultList();
//    }


}
