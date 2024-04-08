package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.ContentRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

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

    public void send (Long userId, Room room, String data) {
        Room findRoom = roomRepository.findById(room.getId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Content content = new Content()
    }
}
