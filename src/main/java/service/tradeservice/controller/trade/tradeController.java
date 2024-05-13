package service.tradeservice.controller.trade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import service.tradeservice.service.RoomService;

@Controller
@Slf4j
public class tradeController {

    @Autowired
    RoomService roomService;

    @GetMapping("/purchaseConfirmation/{roomId}")
    public String purchaseConfirmation(@PathVariable Long roomId) {
        try{
            roomService.buyItemConfirm(roomId);
        } catch (Exception e) {
            return "page/chat/chatPageSellerVer";
        }
        return "page/main";
    }
}
