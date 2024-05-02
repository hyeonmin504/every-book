package service.tradeservice.controller.room;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.controller.chat.ChatForm;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.service.ContentService;
import service.tradeservice.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/page/roomPage")
public class RoomController {
    @Autowired
    ContentService contentService;
    @Autowired
    RoomService roomService;

    //roomList 페이지 roomList 반환
    @GetMapping("/{userId}")
    public String roomPage(@PathVariable Long userId, Model model,
                           @SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm) {
        log.info("getMapping roomPage");
        List<Room> allRoom = roomService.findAllRoom(userId);
        List<RoomListForm> roomList= contentService.findAllRoomAtRoomList(userId,allRoom);

        model.addAttribute("rooms",roomList);
        model.addAttribute("user", loginForm);
        model.addAttribute("userId", userId);

        log.info("roomPage end");
        return "/page/room/roomPage";
    }

    @GetMapping("/{userId}/sell")
    public String sellerRoomPage(@PathVariable Long userId, Model model,
                                 @SessionAttribute(name = "LOGIN_MEMBER", required = false) LoginForm loginForm) {
        List<Room> allRoom = roomService.findAllSellerRoom(userId);
        List<RoomListForm> roomList = contentService.findAllRoomAtRoomList(userId,allRoom);

        model.addAttribute("rooms", roomList);
        model.addAttribute("user", loginForm);
        model. addAttribute("userId", userId);

        return "/page/room/sellRoom";
    }
}
