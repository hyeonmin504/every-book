package service.tradeservice.controller.room;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.controller.room.chat.ChatForm;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ContentService;
import service.tradeservice.service.RoomService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/page/roomPage")
public class RoomController {

    @Autowired
    RoomService roomService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ContentService contentService;
    @Autowired
    UserRepository userRepository;

    //roomList 페이지 roomList 반환
    @GetMapping("/{userId}")
    public String roomPage(@PathVariable Long userId, Model model,
                           @SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm) {
        log.info("getMapping roomPage");

        List<RoomListForm> roomList = new ArrayList<>();

        //Room
        List<Room> rooms = roomService.findAllRoom(userId);
        log.info("room.size={}",rooms.size());
        for (Room room : rooms) {
            log.info("rooms.get()={}",room.getId());
            RoomListForm form = new RoomListForm();
            Content content = contentService.findLastInfo(userId, room.getId());
            //userId,room,content 저장
            User seller = userRepository.findById(room.getItem().getSellerId()).orElseThrow();
            form.setContentInfo(room.getId(),seller,content.getSendDate(),content.getContent());

            //orderStatus 저장
            form.transformStatus(room.getOrder().getOrderStatus());

            //itemName, price 저장
            form.setItemInfo(room.getItem().getItemName(),room.getItem().getPrice());
            roomList.add(form);
        }

        model.addAttribute("rooms",roomList);
        model.addAttribute("user", loginForm);
        model.addAttribute("userId", userId);

        log.info("roomPage end");
        return "/page/room/roomPage";
    }

    //roomList->Room 선택-> 채팅 페이지
    @GetMapping("/{userId}/chatPage/{roomId}")
    public String chatPage(@PathVariable Long userId, @PathVariable Long roomId, Model model,
                           HttpServletRequest request) {
        List<Content> contents = contentService.findChat(userId, roomId);
        List<ChatForm> chatForms = new ArrayList<>();

        for (Content content : contents) {
            chatForms.add(new ChatForm(content.getSendUser(),content.getContent(),content.getSendDate(),content.getRoom().getId()));
        }

        ReceiveChatForm form = new ReceiveChatForm();

        model.addAttribute("chats",chatForms);
        model.addAttribute("userId",userId);
        model.addAttribute("form",form);

        //세션에 채팅 정보 보관
        HttpSession session = request.getSession();
        session.setAttribute("CHAT_INFO", chatForms);
        return "page/room/chatPage";
    }

    @PostMapping("/{userId}/chatPage/{roomId}")
    public String chatSend(@ModelAttribute ReceiveChatForm receiveChatForm,
                           @PathVariable Long userId, @PathVariable Long roomId,
                           RedirectAttributes redirectAttributes,
                           @SessionAttribute(name = "CHAT_INFO",required = false) List<ChatForm> chatForms) {
        List<Content> contents = contentService.findChat(userId, roomId);
        Room room = roomRepository.findById(roomId).orElseThrow();

        contentService.sendChat(userId,room,receiveChatForm.getData());
        for (Content content : contents) {
            chatForms.add(new ChatForm(content.getSendUser(),content.getContent(),content.getSendDate(),content.getRoom().getId()));
        }

        redirectAttributes.addAttribute("userId",userId);
        redirectAttributes.addAttribute("roomId",roomId);

        return "redirect:/page/roomPage/{userId}/chatPage/{roomId}";
    }
}
