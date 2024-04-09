package service.tradeservice.repository.impl;

import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.item.Item;

import java.util.List;

public interface FavoriteRepositoryCustom {
    public List<Favorite> findItemsByUserId(Long userId);

    public Favorite findByUserIdAndItemId(Long userId, Long itemId);
}
