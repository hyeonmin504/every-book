package service.tradeservice.service;

import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.*;
import service.tradeservice.repository.FavoriteRepository;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    //상품 페이지에서 아이템 즐겨찾기를 눌렀을 때
    public Favorite createFavorite(Long userId, Long itemId) {
        log.info("favorite start");
        User user = userRepository.findById(userId).orElseThrow();
        Item item = sellingItemValidation(itemId, user);
        log.info("favorite 생성");
        Favorite favorite = new Favorite(item, user);
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> findFavoriteItem(Long requestUserId){
        List<Favorite> itemsByUserId = favoriteRepository.findItemsByUserId(requestUserId);
        if (itemsByUserId.isEmpty()){
            return new ArrayList<>();
        }
        return itemsByUserId;
    }

    //즐겨찾기 페이지에서 상품을 클릭했을 때
    public Item choiceFavoriteItem(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow();
        Item item = itemRepository.findById(favorite.getItem().getId()).orElseThrow();

        if (item.getRegisterStatus().equals(RegisterStatus.NO_STOCK)){
            throw new NotEnoughStockException("재고가 없습니다");
        }
        if (item.getRegisterStatus().equals(RegisterStatus.COMP)) {
            throw new CompleteTradeException("이미 거래가 완료된 상품입니다");
        }
        if (item.getRegisterStatus().equals(RegisterStatus.CANCEL)) {
            throw new CancelException("취소된 상품입니다");
        }
        if (item.getRegisterStatus().equals(RegisterStatus.SALE)){
            return item;
        }
        throw new NotFoundException("아이템을 찾을 수 없습니다");

    }

    public void deleteFavoriteItem(Long requestUserId, Long selectItemId) {
        Favorite findFavorite = favoriteRepository.findByUserIdAndItemId(requestUserId, selectItemId);
        favoriteRepository.delete(findFavorite);
    }

    private Item sellingItemValidation(Long itemId,User buyer) {
        log.info("SellingItemValidation start");
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();

        //방의 존재 유무
        if (seller.getId().equals(buyer.getId())){
            log.info("판매자 요청={}", seller.getNickName());
            throw new AuthRequitedException(seller.getNickName()+"님의 상품입니다.");
        }
        if (seller.getUniversity() != buyer.getUniversity()){
            throw new AuthRequitedException("학교가 서로 다릅니다");
        }
        if (item.getRegisterStatus() == RegisterStatus.SALE) {
            log.info("SellingItemValidation end 이상 없음");
            return item;
        }
        throw new NotFoundException("아이템을 찾을 수 없습니다");
    }
}

