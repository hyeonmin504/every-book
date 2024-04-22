package service.tradeservice.repository.impl;

import service.tradeservice.controller.item.RegistrationItemForm;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;

import java.util.List;

public interface ItemRepositoryCustom {

    public List<Room> findRoom(Item item);
    public List<Item> findAllByUniv(University univ);

    public List<Item> findMyItem(Long userId);

    //public List<RegistrationItemForm> findItemAndSellerNickName();
}
