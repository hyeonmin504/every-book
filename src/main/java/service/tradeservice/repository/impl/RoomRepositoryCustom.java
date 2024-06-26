package service.tradeservice.repository.impl;

import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;

import java.util.List;

public interface RoomRepositoryCustom {

    public List<Room> findSameRoom(User user, Item item, int orderCount);
    List<Room> findOrderByRoom();
    public List<Room> findByUser(Long userId);
    public List<Room> findBySellerRoom(Long sellerId);
}
