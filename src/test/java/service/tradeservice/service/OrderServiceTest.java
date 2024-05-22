package service.tradeservice.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.ChangeException;
import service.tradeservice.exception.NotEnoughStockException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    RoomService roomService;
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void changeStock() throws Exception {
        //given
        User user1 = new User("hy_min7", University.SAMYOOK,"hyunmin504@naver7.com", "12341234");
        User user2 = new User("hy_min8", University.SAMYOOK,"hyunmin504@naver8.com", "12341234");

        User buyer = userService.join(user1);
        User seller = userService.join(user2);

        Book item = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);

        Book savedItem = itemService.registBook(item, seller.getId());

        Room room = roomService.createRoom(buyer.getId(), item.getId(), 3);
        //when
        orderService.updateStock(room.getOrder().getId(), 2);
        //then
        assertThat(room.getOrder().getStock()).isEqualTo(2);
        assertThrows(NotEnoughStockException.class, () -> orderService.updateStock(room.getOrder().getId(), 4));
        orderService.updateStock(room.getOrder().getId(),0);
        assertThat(room.getState()).isEqualTo(Room.INVISIBLE);
        assertThat(room.getOrder().getOrderStatus()).isEqualTo(Order.TRADE_CANCEL);


        Room room1 = roomService.createRoom(buyer.getId(), item.getId(), 3);
        roomService.sellItemConfirm(room1.getId());
        assertThrows(ChangeException.class, () -> orderService.updateStock(room1.getOrder().getId(), 2));
    }

}