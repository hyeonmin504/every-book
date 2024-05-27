package service.tradeservice.controller.trade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.RoomService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class BuyItemController {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;

    @PostMapping("buyConfirmation/{stringRoomId}")
    public ErrorForm buyConfirmation(@PathVariable String stringRoomId,
                                     @SessionAttribute(name = "LOGIN_MEMBER", required = false) LoginForm loginForm) {
        if (loginForm == null) {
            return new ErrorForm(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.");
        }
        Long roomId = Long.parseLong(stringRoomId);

        User user = userRepository.findByEmail(loginForm.getEmail()).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        CommonException ce = new CommonException(roomService);
        return ce.getConfirmErrorResponse(0,user, room);
    }

    @PostMapping("buyCancel/{stringRoomId}")
    public ErrorForm buyCancel(@PathVariable String stringRoomId,
                               @SessionAttribute(name = "LOGIN_MEMBER", required = false) LoginForm loginForm) {
        if (loginForm == null) {
            return new ErrorForm(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다.");
        }
        Long roomId = Long.parseLong(stringRoomId);
        CommonException ce = new CommonException(roomService);
        return ce.getCancelErrorResponse(loginForm.getId(),roomId);
    }
}
