package service.tradeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ContentServiceTest {
    @Autowired
    ContentService contentService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    RoomService roomService;

    @Test
    @Transactional
    public void 채팅_저장및_가져오기() throws Exception {
        //given
        User user1 = new User("hy_min7", University.SAMYOOK,"hyunmin504@naver7.com", "12341234");
        User user2 = new User("hy_min8", University.SAMYOOK,"hyunmin504@naver8.com", "12341234");

        User buyer = userService.join(user1);
        User seller = userService.join(user2);

        Book item = new Book(Category.BOOK,"책 이름",10000,3 , LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        Book savedItem = itemService.registBook(item, seller.getId());

        Room room = roomService.createRoom(buyer.getId(), item.getId(), 3);
        //when
        contentService.sendChat(buyer.getId(),room,"하이요");
        contentService.sendChat(seller.getId(),room,"ㅎㅇ");

        List<Content> chat = contentService.findChat(buyer.getId(), room.getId());
        //then
        for (Content content : chat) {
            System.out.println("content = " + content);
        }
    }
}