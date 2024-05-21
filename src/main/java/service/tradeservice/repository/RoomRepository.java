package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.impl.RoomRepositoryCustom;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> , RoomRepositoryCustom {

    public List<Room> findByUser(User user);

}
