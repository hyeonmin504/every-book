package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.web.bind.annotation.InitBinder;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @Commit
    public void updateBook() throws Exception {
        //given
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User savedUser = userRepository.save(user);
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
    @Commit
    public void cancelItem() throws Exception {
        //given
        User user = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        Book item = new Book(Category.BOOK,"책 이름",10000,3 ,LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        //when
        User savedUser = userRepository.save(user);
        Book savedItem = itemService.registBook(item, savedUser.getId());


        itemService.cancelBook(savedItem.getId(),savedUser.getId());


    }

}