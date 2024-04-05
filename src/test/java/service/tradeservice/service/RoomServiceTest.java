package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

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
    @Commit
    public void createRoom() throws Exception {
        //given
        User user1 = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2 = new User("hy_min2", University.SAMYOOK,"hyunmin504@naver2.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 , LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User buyer = userService.join(user1);
        User seller = userService.join(user2);

        Book savedItem = itemService.registBook(item, seller.getId());
        //itemService.cancelBook(savedItem.getId(), seller.getId());

        Room room = roomService.createRoom(buyer.getId(), savedItem.getId(),3);
        //then

        assertThat(room.getUser().getId()).isEqualTo(1);
        assertThat(room.getItem().getSellerId()).isEqualTo(2);

    }

}