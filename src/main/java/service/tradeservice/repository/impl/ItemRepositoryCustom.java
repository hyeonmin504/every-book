package service.tradeservice.repository.impl;

import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;

import java.util.List;

public interface ItemRepositoryCustom {

    public List<Room> findRoom(Item item);
}
