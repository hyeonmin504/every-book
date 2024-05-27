package service.tradeservice;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ItemService;
import service.tradeservice.service.RoomService;
import service.tradeservice.service.UserService;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final UserService userService;
    private final ItemService itemService;
    private final RoomService roomService;

    @PostConstruct
    public void init() {
        User user = new User("hy", University.SAMYOOK,"111@naver.com","111");
        User user2 = new User("dong", University.SAMYOOK,"222@naver.com","222");
        userService.join(user);
        userService.join(user2);

        Book book = new Book(Category.BOOK,"itemName",10000,10,"author","samyook","20190101",1,1);
        Book book1 = itemService.registBook(book, user.getId());

        Room room = roomService.createRoom(user2.getId(), book1.getId(), 3);

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {

        }
    }
}