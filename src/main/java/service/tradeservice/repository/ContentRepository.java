package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Room;
import service.tradeservice.repository.impl.ContentRepositoryCustom;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {

}
