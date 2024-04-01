package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.impl.UserRepositoryCustom;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    public List<User> findByEmail(String email);
    public List<User> findByNickName(String nickName);
}
