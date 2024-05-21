package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.DuplicationUserException;
import service.tradeservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User join(User user) {
        User savedUser = userRepository.save(user);
        log.info("join User={}",user.getId());
        return savedUser;
    }

    public User login(String email,String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }


    public String checkDuplicateUser(User user) {
        List<User> findEmail = userRepository.findByEmail(user.getEmail());
        List<User> findName = userRepository.findByNickName(user.getNickName());
        if (!findName.isEmpty()){
            log.info("이미 존재하는 닉네임입니다");
            return "sameNick";
        }
        if (!findEmail.isEmpty()){
            log.info("이미 존재하는 회원입니다.");
            return "sameEmail";
        }
        return "ok";
    }
}
