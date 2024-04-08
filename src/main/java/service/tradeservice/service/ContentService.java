package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.repository.ContentRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContentService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentRepository contentRepository;

    @Transactional
    public void saveChat (Long userId, Room room, String data) {
        Room findRoom = roomRepository.findById(room.getId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Content content = new Content(userId, room, data);
        contentRepository.save(content);
    }

    public List<Content> findChat(Long userId, Long roomId){
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        //요청자가 판매자 or 구매자 이어야 한다.
        if (room.getUser().getId().equals(user.getId()) || room.getItem().getSellerId().equals(user.getId())){
            return contentRepository.findSendChat(roomId);
        }
        throw new AuthRequitedException("볼 권한이 없습니다");
    }
}
