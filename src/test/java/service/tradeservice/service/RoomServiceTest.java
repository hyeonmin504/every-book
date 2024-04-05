package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Orders;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.CancelException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor
class RoomServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    @Test
    @Transactional
    public void create_cancel() throws Exception {
        //given
        User user1 = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2 = new User("hy_min2", University.SAMYOOK,"hyunmin504@naver2.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 , LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        User buyer = userService.join(user1);
        User seller = userService.join(user2);

        Book savedItem = itemService.registBook(item, seller.getId());
        //itemService.cancelBook(savedItem.getId(), seller.getId());

        //when -> 이미 존재하는 방을 취소했을 때
        Room room = roomService.createRoom(buyer.getId(), savedItem.getId(),3);
        roomService.CancelTradeRoom(buyer.getId(),room.getId());

        Room room2 = roomService.createRoom(buyer.getId(), savedItem.getId(),3);

        //then 1 -> 같은 방을 다시 만들었을 경우 똑같은 방으로 반환
        assertThat(room.getId()).isEqualTo(room2.getId());

        //when 1.5 -> 구매자가 먼저 구매 확정을 눌렀을 때
        roomService.buyItemConfirm(room.getId());
        //then 1.5 -> 판매자 부터 눌러야 해서 TRADING 상태 그대로 남음
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Orders.TRADING);

        //when 2 -> 판매자가 판매 확정을 눌렀을 때
        roomService.sellItemConfirm(room2.getId());

        //then 2 -> 만약 orderCount == stockQuantity + SELL_CONFIRM 일 경우 No_STOCK
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Orders.SELL_CONFIRM);
        assertThat(room.getItem().getRegisterStatus()).isEqualTo(RegisterStatus.NO_STOCK);

        //when 3 -> 판매자가 판매 취소를 했을 때
        //then 3 -> CancelException
        assertThrows(CancelException.class, () -> roomService.CancelTradeRoom(seller.getId(),room.getId()));

        //when 3.5 -> 구매자가 판매 취소를 했을 때
        roomService.CancelTradeRoom(buyer.getId(),room.getId());

        //then 3.5 -> 채팅 목록에서 INVISIBLE = 1로 바뀜
        assertThat(room.getState()).isEqualTo(1);

        Room room3 = roomService.createRoom(buyer.getId(), savedItem.getId(),3);
        roomService.sellItemConfirm(room.getId());

        //when 4 -> 구매자가 구매 확정을 눌렀을 때
        roomService.buyItemConfirm(room.getId());

        //then 4 -> 채팅방 상태가 거래 완료로 변경, 상품의 상태는 판매 완료로 변경
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Orders.TRADE_COMP);
        assertThat(room.getItem().getRegisterStatus()).isEqualTo(RegisterStatus.COMP);

        //when 5 -> 채팅방 삭제를 눌렀을 경우
        roomService.CancelTradeRoom(buyer.getId(),room.getId());

        //then 3.5 -> 채팅 목록에서 INVISIBLE = 1로 바뀜
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Orders.TRADE_CANCEL);
    }
}