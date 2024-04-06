package service.tradeservice.repository.impl;

import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;

import java.util.List;

public interface RoomRepositoryCustom {
    public void validationSellerId(Long sellerId);

    public List<Room> findSameRoom(Long userId, Long itemId, int orderCount);
}
