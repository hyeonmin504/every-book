package service.tradeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.impl.UserRepositoryCustom;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    public Optional<User> findByEmail(String email);
    public List<User> findByNickName(String nickName);
    public User findByEmailAndPassword(String email,String password);
}
