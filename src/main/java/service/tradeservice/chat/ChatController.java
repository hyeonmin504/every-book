package service.tradeservice.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.controller.item.BookInfoForm;
import service.tradeservice.controller.room.CreateRoomForm;
import service.tradeservice.domain.Content;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ContentService;
import service.tradeservice.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/page/items")
public class ChatController {

    private final ChatService service;

    @PostMapping("/{userId}/item/BOOK/{itemId}")
    public ChatRoom createRoom(@PathVariable Long userId, @PathVariable Long itemId,
                               @RequestParam int stock){
        return service.createRoom(userId,itemId,stock);
    }

    @GetMapping("/{userId}/chatPage/{roomId}")
    public List<ChatRoom> findAllRooms(@PathVariable Long userId, @PathVariable Long roomId){
        return service.findAllRoom(userId,roomId);
    }
}
