package service.tradeservice.chat;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ContentService;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatRoom {

    @Autowired
    UserRepository userRepository;

    private Long roomId; // 채팅방 아이디
    private String userNickName;
    private Integer stock;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(Long roomId, String userNickName, int stock){
        this.roomId = roomId;
        this.userNickName = userNickName;
        this.stock = stock;
    }
}
