package service.tradeservice.repository.impl;

import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;

import java.util.List;

public interface ContentRepositoryCustom {

    public List<Content> findSendChat(Long roomId,User buyer,Long seller);
    public List<Content> findByRoom(Room room);

    public List<Content> findLastChatInfo(Room room, User user);

    List<Content> findLastChatInfoSellerVer(Room room, Long user);

}
