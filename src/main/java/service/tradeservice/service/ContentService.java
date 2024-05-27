package service.tradeservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.controller.room.RoomListForm;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.repository.ContentRepository;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentService{

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    ItemRepository itemRepository;

    @Transactional
    public void sendChat (Long userId, Room room, String data) {
        Room findRoom = roomRepository.findById(room.getId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Content content = new Content(user.getNickName(), room, data);
        contentRepository.save(content);
    }

    @Transactional
    public List<Content> findChat(Long userId, Long roomId){
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        Item item = itemRepository.findById(room.getItem().getId()).orElseThrow();
        //요청자가 판매자 or 구매자 이어야 한다.
        if (room.getUser().getId().equals(user.getId()) || room.getItem().getSellerId().equals(user.getId())){
            return contentRepository.findByRoom(room);
        }

        throw new AuthRequitedException("볼 권한이 없습니다");
    }

    public List<RoomListForm> findAllRoomAtRoomList(Long userId,List<Room> allRoom){
        List<RoomListForm> forms = new ArrayList<>();
        for (Room room : allRoom) {
            User seller = userRepository.findById(room.getItem().getSellerId()).orElseThrow();
            if (seller.getId().equals(userId)) {
                Content content = findLastInfoSellerVer(seller.getId(),room.getId());
                forms.add(new RoomListForm(room.getOrder().getOrderStatus(),room.getId(),room.getOrder().getStock(),content.getSendDate(),content.getContent(),room.getItem().getItemName(),room.getItem().getPrice()));
            } else if (room.getUser().getId().equals(userId)) {
                Content content = findLastInfo(userId, room.getId());
                forms.add(new RoomListForm(room.getOrder().getOrderStatus(),room.getId(),room.getOrder().getStock(),content.getSendDate(),content.getContent(),room.getItem().getItemName(),room.getItem().getPrice()));
            }
        }
        return forms;
    }

    private Content findLastInfoSellerVer(Long sellerId, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        List<Content> lastChatInfo = contentRepository.findLastChatInfoSellerVer(room,sellerId);
        return lastChatInfo.get(lastChatInfo.size()-1);
    }

    public Content findLastInfo(Long buyerId,Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(buyerId).orElseThrow();
        List<Content> lastChatInfo = contentRepository.findLastChatInfo(room, user);
        return lastChatInfo.get(lastChatInfo.size()-1);
    }
}
