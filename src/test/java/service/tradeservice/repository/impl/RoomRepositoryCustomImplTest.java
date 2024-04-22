package service.tradeservice.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ItemService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomRepositoryCustomImplTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void findRoom() throws Exception {
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,"저자", "출판사","20240402",2, 2);

        //when
        User savedUser = userRepository.save(user);
        Book savedItem = itemService.registBook(item, savedUser.getId());


    }
}