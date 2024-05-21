package service.tradeservice.service;

import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemService itemService;
    @Autowired
    EntityManager em;
    @Autowired
    RoomService roomService;

    @Test
    @Transactional
    public void findUserByEmailAndPassword() throws Exception {
        //given
        User user = new User("kim", University.SAMYOOK,"hyunmin@naver.com","111");
        //when
        userService.join(user);
        User login = userService.login(user.getEmail(), user.getPassword());
        //then
        Assertions.assertThat(login).isEqualTo(user);
    }

    @Test
    @Commit
    @Transactional
    public void toManyTest() throws Exception {
        //given
        User user = new User("123@naver.com","123123");
        User buyer = new User("111@naver.com","123123");
        User savedUser = userService.join(user);
        User savedUser2 = userService.join(buyer);

        Book item = new Book(Category.BOOK,"책 이름",10000,3 , "저자", "출판사","20240402",2, 2);
        Book item2 = new Book(Category.BOOK,"책 이름",10000,5 , "저자", "출판사","20240402",2, 2);
        Book item3 = new Book(Category.BOOK,"책 이름",10000,7 , "저자", "출판사","20240402",2, 2);
        Book savedBook = itemService.registBook(item, savedUser.getId());
        Book savedBook2 = itemService.registBook(item2, savedUser.getId());
        Book savedBook3 = itemService.registBook(item3, savedUser.getId());

        Room room = roomService.createRoom(savedUser2.getId(), savedBook.getId(), 1);
        Room room2 = roomService.createRoom(savedUser2.getId(), savedBook2.getId(), 1);
        Room room3 = roomService.createRoom(savedUser2.getId(), savedBook3.getId(), 1);

        em.flush();
        em.clear();
        
        System.out.println("when");
        
        //when
        List<User> users = userRepository.findAll();
        System.out.println("UserServiceTest.toManyTest");


        List<UserDto> userDtos = users.stream()
                        .map(u -> new UserDto(u))
                                .collect(toList());
//        List<Room> orderByRoom = roomRepository.findOrderByRoom();
//        for (Room room1 : orderByRoom) {
//            System.out.println("room1 = " + room1.getId());
//        }
        //then

        Assertions.assertThat(userDtos.size()).isEqualTo(2);
    }

    @Data
    static class UserDto {
        private String nickName;
        private List<RoomDto> rooms = new ArrayList<>();

        public UserDto(User user) {
            this.nickName = user.getNickName();
            this.rooms = user.getRooms().stream()
                    .map(RoomDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class RoomDto {
        private int state;
        private OrderDto order;

        public RoomDto(Room room) {
            this.state = room.getState();
            if (room.getOrder() != null) {
                this.order = new OrderDto(room.getOrder());
            }
        }
    }

    @Data
    static class OrderDto {
        private int price;

        public OrderDto(Order order) {
            this.price = order.getPrice();
        }
    }

}