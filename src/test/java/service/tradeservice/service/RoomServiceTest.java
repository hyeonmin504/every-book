package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.ChangeException;
import service.tradeservice.exception.NotEnoughStockException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
class RoomServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;

    @Test
    @Transactional
    @Commit
    public void create_cancel() throws Exception {
        //given
        User user1 = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "12341234");
        User user2 = new User("hy_min2", University.SAMYOOK,"hyunmin504@naver2.com", "12341234");
        User user3 = new User("hy_min3", University.SAMYOOK,"hyunmin504@naver3.com", "12341234");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);
        Book item2 = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);

        User buyer = userService.join(user1);
        User seller = userService.join(user2);
        User buyer2 = userService.join(user3);

        Book savedItem = itemService.registBook(item, seller.getId());
        Book savedItem2 = itemService.registBook(item2, seller.getId());
        //itemService.cancelBook(savedItem.getId(), seller.getId());

        //when 1 -> 이미 존재하는 방을 취소했을 때
        Room room = roomService.createRoom(buyer.getId(), savedItem.getId(),3);
        roomService.CancelTradeRoom(buyer.getId(),room.getId());

        Room room2 = roomService.createRoom(buyer.getId(), savedItem.getId(),3);

        //then 1 -> 같은 방을 다시 만들었을 경우 똑같은 방으로 반환
        assertThat(room.getId()).isEqualTo(room2.getId());

        //when 1.5 -> 구매자가 먼저 구매 확정을 눌렀을 때
        roomService.buyItemConfirm(room.getId());
        //then 1.5 -> 판매자 부터 눌러야 해서 TRADING 상태 그대로 남음
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Order.TRADING);

        //when 2 -> 판매자가 판매 확정을 눌렀을 때
        roomService.sellItemConfirm(room2.getId());

        //then 2 -> 만약 orderCount == stockQuantity + SELL_CONFIRM 일 경우 No_STOCK
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Order.SELL_CONFIRM);
        assertThat(room.getItem().getRegisterStatus()).isEqualTo(RegisterStatus.NO_STOCK);
        
        //when 3.5 -> 구매자가 판매 취소를 했을 때
        roomService.CancelTradeRoom(buyer.getId(),room.getId());

        //then 3.5 -> 채팅 목록에서 INVISIBLE = 1로 바뀜
        assertThat(room.getState()).isEqualTo(1);

        Room room3 = roomService.createRoom(buyer2.getId(), savedItem2.getId(),3);
        roomService.sellItemConfirm(room3.getId());

        //when 4 -> 구매자가 구매 확정을 눌렀을 때
        roomService.buyItemConfirm(room3.getId());

        //then 4 -> 채팅방 상태가 거래 완료로 변경, 상품의 상태는 판매 완료로 변경
        assertThat(room3.getOrder().getOrderStatus()).isEqualTo(Order.TRADE_COMP);
        assertThat(room3.getItem().getRegisterStatus()).isEqualTo(RegisterStatus.COMP);

        //when 5 -> 채팅방 삭제를 눌렀을 경우
        roomService.CancelTradeRoom(buyer2.getId(),room3.getId());

        //then 3.5 -> 채팅 목록에서 INVISIBLE = 1로 바뀜
        assertThat(room3.getOrder().getOrderStatus()).isEqualTo(Order.TRADE_CANCEL);

    }
    
    @Test
    public void throw_catch() throws Exception {
        //given
        User user1 = new User("hy_min4", University.SAMYOOK,"hyunmin504@naver4.com", "12341234");
        User user2 = new User("hy_min5", University.SAMYOOK,"hyunmin504@naver5.com", "12341234");
        User user3 = new User("hy_min6", University.SEOUL,"hyunmin504@naver6.com", "12341234");

        User buyer = userService.join(user1);
        User seller = userService.join(user2);
        User seller2 = userService.join(user3);

        Book item = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);
        Book item2 = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);
        Book item3 = new Book(Category.BOOK,"책 이름",10000,3 ,  "저자", "출판사","20240402",2, 2);

        Book savedItem = itemService.registBook(item, seller.getId());

        Room room = roomService.createRoom(buyer.getId(), savedItem.getId(),3);

        //createRoom
        //throw new AuthRequitedException("판매자와 구매자가 같을 수 없습니다");
        assertThrows(AuthRequitedException.class, () -> roomService.createRoom(seller.getId(),savedItem.getId(),1));

        //throw new AuthRequitedException("학교가 서로 다릅니다");
        Book savedItem3 = itemService.registBook(item3, seller2.getId());
        assertThrows(AuthRequitedException.class, () -> roomService.createRoom(buyer.getId(),savedItem3.getId(), 3));

        //throw new AuthRequitedException("이미 방이 존재합니다");
        assertThrows(AuthRequitedException.class, () -> roomService.createRoom(buyer.getId(),savedItem.getId(),3));

        //throw new CancelException("이미 판매된 상품이어서 방을 생성할 수 없습니다");
        Book savedItem2 = itemService.registBook(item2, seller.getId());
        Room room1 = roomService.createRoom(buyer.getId(), savedItem2.getId(), 3);
        roomService.sellItemConfirm(room1.getId());
        roomService.buyItemConfirm(room1.getId());
        assertThrows(NotEnoughStockException.class, () -> roomService.createRoom(buyer.getId(),savedItem2.getId(),3));


        //CancelTradeRoom
        //throw new CancelException("판매자는 거래중일 때 방을 삭제할 수 없습니다.");
        roomService.sellItemConfirm(room.getId());
        assertThrows(CancelException.class, () -> roomService.CancelTradeRoom(seller.getId(),room.getId()));


        //sellItemConfirm
        //throw new ChangeException("판매가 취소된 채팅방입니다.");
        roomService.CancelTradeRoom(buyer.getId(),room.getId());
        assertThrows(ChangeException.class, () -> roomService.sellItemConfirm(room.getId()));


        //buyItemConfirm
        Room room2 = roomService.createRoom(buyer.getId(), savedItem.getId(), 3);
        roomService.sellItemConfirm(room2.getId());
        //throw new ChangeException("판매가 취소된 채팅방입니다.");
        roomService.CancelTradeRoom(buyer.getId(),room.getId());
        assertThrows(ChangeException.class, () -> roomService.buyItemConfirm(room2.getId()));
    }


}