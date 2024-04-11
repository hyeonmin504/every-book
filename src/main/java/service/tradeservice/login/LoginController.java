package service.tradeservice.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.UserService;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        log.info("login,GetMapping");
        //User user1 = userRepository.findById(id).orElseThrow();
        //form.setEmail(user1.getEmail());
        return "login/loginForm";
    }

    @PostMapping(value = "/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        User login = userService.login(form.getEmail(), form.getPassword());
        if (login == null) {
            bindingResult.rejectValue(null,"loginFail",null, "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        return "redirect:/page/main";
    }

    @GetMapping(value = "/page/main")
    public String main() {
        log.info("login/main");
        return "/page/main";
    }

    @GetMapping(value = "/user/add")
    public String signUpForm(@ModelAttribute("signUpForm") SignUpForm form,
                             Model model) {

        model.addAttribute("university", SignUpForm.university());
        return "user/addUser";
    }

    @PostMapping(value = "/user/add")
    public String signUp(@Validated @ModelAttribute("signUpForm") SignUpForm form,
                         BindingResult bindingResult, Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("hasErrors");
            model.addAttribute("university", SignUpForm.university());
            return "user/addUser";
        }

        User user = new User(form.getNickName(),form.getUniversity(),form.getEmail(),form.getPassword());
        User join = userService.join(user);
        redirectAttributes.addAttribute("itemId",join.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/login";
    }
}
