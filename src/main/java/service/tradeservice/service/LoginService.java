package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.DuplicationUserException;
import service.tradeservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        if (CheckUser(user)) {
            throw new DuplicationUserException("duplication email or nickname");
        }
        userRepository.save(user);
    }

    private Boolean CheckUser(User user) {
        List<User> findEmail = userRepository.findByEmail(user.getEmail());
        List<User> findName = userRepository.findByNickName(user.getNickName());
        if (!findName.isEmpty()||!findEmail.isEmpty()){
            return true;
        }
        return false;
    }
}
