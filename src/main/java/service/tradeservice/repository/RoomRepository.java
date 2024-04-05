package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Room;
import service.tradeservice.repository.impl.RoomRepositoryCustom;

public interface RoomRepository extends JpaRepository<Room,Long> , RoomRepositoryCustom {
}
