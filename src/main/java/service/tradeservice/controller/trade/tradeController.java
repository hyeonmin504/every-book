package service.tradeservice.controller.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.domain.user.User;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.RoomService;
import service.tradeservice.service.UserService;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class tradeController {

    private final RoomService roomService;
    private final UserRepository userRepository;


    //@GetMapping("/purchaseConfirmation/{roomId}")
    public String purchaseConfirmation(@PathVariable String roomId, RedirectAttributes redirectAttributes,
                                       @SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm) {
        User user = userRepository.findByEmail(loginForm.getEmail()).orElseThrow();
        try{
            roomService.buyItemConfirm(Long.parseLong(roomId));
            redirectAttributes.addAttribute("userId",user.getId());
            redirectAttributes.addAttribute("roomId",roomId);

            return "redirect:/purchaseConfirmation/{roomId}";
        } catch (Exception e) {
            return "page/chat/chatPageSellerVer";
        }
    }

    //@PostMapping("/purchaseConfirmation/{roomId}")
    public String purchaseConfirmationLogic(@PathVariable Long roomId, RedirectAttributes redirectAttributes,
                                            @SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm) {
        User user = userRepository.findByEmail(loginForm.getEmail()).orElseThrow();
        try{
            redirectAttributes.addAttribute("userId",user.getId());
            redirectAttributes.addAttribute("roomId",roomId);

            return "redirect:/purchaseConfirmation/{roomId}";
        } catch (Exception e) {
            return "page/chat/chatPageSellerVer";
        }
    }
}
