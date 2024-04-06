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

    @Autowired
    UserRepository userRepository;

    @Transactional
    public User join(User user) {
        user.setJoinDay();
        User savedUser = userRepository.save(user);
        log.info("join User={}",user.getId());
        return savedUser;
    }


    private void CheckDuplicateUser(User user) {
        List<User> findEmail = userRepository.findByEmail(user.getEmail());
        List<User> findName = userRepository.findByNickName(user.getNickName());
        if (!findName.isEmpty()){
            throw new DuplicationUserException("이미 존재하는 닉네임입니다");
        }
        if (!findEmail.isEmpty()){
            throw new DuplicationUserException("이미 존재하는 회원입니다.");
        }
    }
}
