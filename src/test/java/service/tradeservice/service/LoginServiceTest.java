package service.tradeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.DuplicationUserException;

@SpringBootTest
@Slf4j
public class LoginServiceTest {

    @Autowired
    UserService loginService;

    @Test
    @Commit
    public void checkUser() throws Exception {
        //given
        User user1 = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2= new User("hy_min", University.SAMYOOK,"hyunmin504@neaver.com", "qwer");
        //when
        loginService.join(user1);
        //then
        Assertions.assertThrows(DuplicationUserException.class, () -> loginService.join(user2));

    }

}