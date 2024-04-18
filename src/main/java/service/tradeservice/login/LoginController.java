package service.tradeservice.login;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
        return "login/loginForm";
    }

    @PostMapping(value = "/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        User login = userService.login(form.getEmail(), form.getPassword());
        if (login == null) {
            bindingResult.rejectValue(null,"loginFail",null, "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }
        log.info("회원 존재");
        form.setNickName(login.getNickName());

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute("LOGIN_MEMBER", form);

        return "redirect:/page/main";
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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info("logout");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }


}
