package service.tradeservice.controller.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.*;
import service.tradeservice.service.RoomService;

@RequiredArgsConstructor
@Component
public class CommonException {

    @Autowired
    private final RoomService roomService;

    /**
     *
     * @param divide 0 -> 구매자, 1-> 판매자
     * @param user -> 요청자
     * @param room ->
     * @return
     */
    public ErrorForm getConfirmErrorResponse(int divide, User user, Room room) {

        if (user == null) {
            return new ErrorForm(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.");
        }
        try {
            if (divide == 0) roomService.buyItemConfirm(room.getId());
            else roomService.sellItemConfirm(room.getId());

            return new ErrorForm(HttpStatus.OK.value(), "판매가 성공적으로 확정되었습니다.");
        } catch (NotEnoughStockException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "재고 수량이 부족합니다 현재 재고 수량:" + room.getItem().getStockQuantity());
        } catch (NumberFormatException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "잘못된 방 ID입니다.");
        } catch (ChangeException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "거래가 취소된 방입니다.");
        } catch (CompleteTradeException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "거래가 성공적으로 끝났습니다");
        } catch (ConfirmException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "이미 거래 확정했습니다");
        } catch (SequenceException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(), "판매자가 먼저 판매 확정을 눌러야 합니다.");
        }
    }

    public ErrorForm getCancelErrorResponse(Long userId, Long roomId) {
        if (userId == null) {
            return new ErrorForm(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다.");
        }
        try {
            roomService.cancelTradeRoom(userId,roomId);
            return new ErrorForm(HttpStatus.OK.value(),"거래가 취소되었습니다");
        } catch (CancelException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(),"판매자는 거래중일 때 방을 삭제할 수 없습니다.");
        } catch (CompleteTradeException e) {
            return new ErrorForm(HttpStatus.BAD_REQUEST.value(),"거래가 완료상태일 때 방을 삭제할 수 없습니다.");
        }
    }
}
