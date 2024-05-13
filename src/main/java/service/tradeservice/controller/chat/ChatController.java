package service.tradeservice.controller.chat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ContentService;
import service.tradeservice.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/page/roomPage")
public class ChatController {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentService contentService;


    //roomList->Room 선택-> 채팅 페이지
    @GetMapping("/{userId}/chatPage/{roomId}")
    public String chatPage(@PathVariable Long userId, @PathVariable Long roomId, Model model,
                           HttpServletRequest request,
                           @ModelAttribute ChatForm form) {
        List<Content> contents = contentService.findChat(userId, roomId);
        List<ChatForm> chatForms = new ArrayList<>();

        for (Content content : contents) {
            chatForms.add(new ChatForm(content.getSendUser(),content.getContent(),content.getSendDate(),content.getRoom().getId()));
        }

        model.addAttribute("chats",chatForms);
        model.addAttribute("userId",userId);
        model.addAttribute("form",form);
        model.addAttribute("roomId",roomId);

        //세션에 채팅 정보 보관
        HttpSession session = request.getSession();
        session.setAttribute("CHAT_INFO", chatForms);

        Long sellerId = roomRepository.findById(roomId).orElseThrow().getItem().getSellerId();
        if (userRepository.findById(userId).orElseThrow().getId().equals(sellerId)){
            return "page/chat/chatPageSellerVer";
        }
        return "page/chat/chatPageBuyerVer";
    }

    @PostMapping("/{userId}/chatPage/{roomId}")
    public String chatSend(@PathVariable Long userId, @PathVariable Long roomId,
                           @ModelAttribute ChatForm form,
                           RedirectAttributes redirectAttributes,
                           @SessionAttribute(name = "CHAT_INFO",required = false) List<ChatForm> chatForms) {
        List<Content> contents = contentService.findChat(userId, roomId);
        Room room = roomRepository.findById(roomId).orElseThrow();

        for (Content content : contents) {
            chatForms.add(new ChatForm(content.getSendUser(),content.getContent(),content.getSendDate(),content.getRoom().getId()));
        }

        contentService.sendChat(userId,room,form.getMessage());

        redirectAttributes.addAttribute("userId",userId);
        redirectAttributes.addAttribute("roomId",roomId);

        return "redirect:/page/roomPage/{userId}/chatPage/{roomId}";
    }
}
