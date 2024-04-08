package service.tradeservice.service;

import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.CompleteTradeException;
import service.tradeservice.exception.NotEnoughStockException;
import service.tradeservice.exception.NotFoundException;
import service.tradeservice.repository.FavoriteRepository;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    public List<Item> findFavoriteItem(Long requestUserId){
        return favoriteRepository.findByUserId(requestUserId);
    }

    public Item selectFavoriteItem(Long favoriteId) {
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
}
