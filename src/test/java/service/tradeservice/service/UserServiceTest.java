package service.tradeservice.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Test
    @Transactional
    public void findUserByEmailAndPassword() throws Exception {
        //given
        User user = new User("kim", University.SAMYOOK,"hyunmin@naver.com","111");
        //when
        userService.join(user);
        User login = userService.login(user.getEmail(), user.getPassword());
        //then
        Assertions.assertThat(login).isEqualTo(user);
    }


}