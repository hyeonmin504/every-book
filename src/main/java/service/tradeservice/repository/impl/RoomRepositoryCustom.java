package service.tradeservice.repository.impl;

import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;

import java.util.List;

public interface RoomRepositoryCustom {

    public List<Room> findSameRoom(User user, Item item, int orderCount);

    public List<Room> findByUser(Long userId);
}
