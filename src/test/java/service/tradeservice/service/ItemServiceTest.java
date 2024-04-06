package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.web.bind.annotation.InitBinder;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.NotEnoughStockException;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomService roomService;

    @Test
    @Commit
    public void updateBook() throws Exception {
        //given
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User savedUser = userService.join(user);
        Book savedItem = itemService.registBook(item, savedUser.getId());

        Book newItem = new Book(Category.BOOK,"책 이름",20000,2 ,LocalDateTime.now(), RegisterStatus.COMP, "저자", "출판사","20240402",2, 2);
        User findUser = userRepository.findById(savedUser.getId()).orElseThrow();
        Book book = itemService.updateBook(newItem, savedItem.getId(), findUser.getId());

        Item findItem = itemRepository.findById(savedItem.getId()).orElseThrow();
        //then
        assertThat(item.getId()).isEqualTo(findItem.getId());
        assertThat(book.getPrice()).isEqualTo(20000);

    }

    @Test
    public void stock_quantity_exception() throws Exception {
        //given
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2 = new User("hy_min2", University.SAMYOOK,"hyunmin5047@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User savedUser = userService.join(user);
        User savedUser2 = userService.join(user2);
        Book savedItem = itemService.registBook(item, savedUser.getId());

        assertThrows(NotEnoughStockException.class, () -> roomService.createRoom(savedUser2.getId(),item.getId(), 4));
    }

    @Test
    public void before_delete_check_room() throws Exception {
        //given
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2 = new User("hy_min2", University.SAMYOOK,"hyunmin5047@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User seller= userService.join(user);
        User buyer = userService.join(user2);
        Book savedItem = itemService.registBook(item, seller.getId());

        Room room = roomService.createRoom(buyer.getId(), savedItem.getId(), 3);
        //then 1 -> 채팅방이 존재하면 상품 취소가 불가능합니다
        assertThrows(CancelException.class, () -> itemService.cancelBook(savedItem.getId(), seller.getId()));

        roomService.CancelTradeRoom(buyer.getId(),room.getId());
        //then 2 -> throw new AuthRequitedException("수정 권한이 없습니다");
        assertThrows(AuthRequitedException.class, () -> itemService.cancelBook(savedItem.getId(), buyer.getId()));

        //then 3 -> throw new CancelException("완료 상태에서는 등록 취소가 불가능 합니다. 현재 상태 = "+ book.getRegisterStatus());
        Room room1 = roomService.createRoom(buyer.getId(), savedItem.getId(), 3);
        roomService.sellItemConfirm(room1.getId());
        roomService.buyItemConfirm(room1.getId());
        assertThrows(CancelException.class, () -> itemService.cancelBook(savedItem.getId(), seller.getId()));

    }

}