package service.tradeservice.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.ContentRepository;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ContentService;
import service.tradeservice.service.RoomService;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;
    @Autowired
    ContentService contentService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    RoomService roomService;

    private final ObjectMapper mapper;
    private Map<Long, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRoom(Long userId, Long roomId){
        List<Room> all = roomRepository.findAll();
        String nickName = userRepository.findById(userId).orElseThrow().getNickName();
        List<ChatRoom> forms = new ArrayList<>();
        for (Room room : all) {
            forms.add(new ChatRoom(roomId,nickName,room.getOrder().getStock()));
        }
        return forms;
    }

    public ChatRoom findRoomById(Long roomId){
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(Long buyerId,Long itemId, int count) {
        Room room = roomService.createRoom(buyerId, itemId, count);

        ChatRoom chatRoom = new ChatRoom(room.getId(),room.getUser().getNickName(),room.getOrder().getStock());

        // Builder 를 이용해서 ChatRoom 을 Building
        ChatRoom.builder()
                .roomId(chatRoom.getRoomId())
                .userNickName(chatRoom.getUserNickName())
                .build();

        chatRooms.put(chatRoom.getRoomId(),chatRoom);
        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}