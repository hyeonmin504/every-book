package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.item.Item;
import service.tradeservice.repository.impl.FavoriteRepositoryCustom;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>, FavoriteRepositoryCustom {

}
