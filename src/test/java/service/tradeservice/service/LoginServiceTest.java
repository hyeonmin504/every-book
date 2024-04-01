package service.tradeservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;

@SpringBootTest
public class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Test
    public void checkUser() throws Exception {
        //given
        User user1 = new User("hy_min", University.SAMYOOK,"hyunmin504@naver.com", "hoon0504~");
        User user2= new User("who", University.SAMYOOK,"hyunmin504@neaver.com", "qwer");
        //when
        loginService.saveUser(user1);
        loginService.saveUser(user2);
        //then


    }

}