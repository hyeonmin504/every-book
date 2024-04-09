package service.tradeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.FavoriteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    FavoriteService favoriteService;
    @Autowired
    FavoriteRepository favoriteRepository;
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
    public void 즐찾_기능_테스트() throws Exception {
        //given
        User user1 = new User("hy_min7", University.SAMYOOK,"hyunmin504@naver7.com", "12341234");
        User user2 = new User("hy_min8", University.SAMYOOK,"hyunmin504@naver8.com", "12341234");

        User buyer = userService.join(user1);
        User seller = userService.join(user2);

        Book item = new Book(Category.BOOK,"책 이름",10000,3 , LocalDateTime.now(), RegisterStatus.SALE, "저자", "출판사","20240402",2, 2);

        Book savedItem = itemService.registBook(item, seller.getId());

        Room room = roomService.createRoom(buyer.getId(), item.getId(), 3);
        //when
        Favorite favorite = favoriteService.createFavorite(buyer.getId(), savedItem.getId());

        //then 1 -> createFavorite
        assertThat(favorite.getId()).isEqualTo(1);

        //then 2 -> findFavoriteItem
        List<Favorite> favoriteItems = favoriteService.findFavoriteItem(buyer.getId());
        for (Favorite favorite1 : favoriteItems) {
            assertThat(favorite1.getUser().getNickName()).isEqualTo("hy_min7");
        }

        //then 3 -> choiceFavoriteItem

        Item findItem = favoriteService.choiceFavoriteItem(favoriteItems.get(0).getId());
        assertThat(findItem).isEqualTo(item);

        favoriteService.deleteFavoriteItem(buyer.getId(),findItem.getId());
        List<Favorite> favoriteItem2 = favoriteService.findFavoriteItem(buyer.getId());
        assertThat(favoriteItem2).isEqualTo(new ArrayList<>());
    }
}