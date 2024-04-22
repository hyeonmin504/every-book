package service.tradeservice.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import service.tradeservice.domain.user.User;

public interface LoginRepository extends JpaRepository<User, Long > {


}
