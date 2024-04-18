package service.tradeservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.tradeservice.login.LoginForm;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/page")
public class PageController {

    @GetMapping(value = "/main")
    public String loginMainPage(@SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm, Model model) {
        log.info("login/main");

        if (loginForm == null) {
            log.info("loginUser==null");
            return "/page/main";
        }

        //세션 유지
        model.addAttribute("user", loginForm);
        return "/page/main";
    }

    @PostMapping(value = "/main")
    public String mainPage() {
        return "redirect:/page/main";
    }

    @GetMapping("/myPage")
    public String myPage() {
        log.info("getMapping myPage");
        return "/page/myPage";
    }

    @GetMapping("/roomPage")
    public String roomPage() {
        log.info("getMapping roomPage");
        return "/page/roomPage";
    }

    @GetMapping("/sellPage")
    public String sellPage() {
        log.info("getMapping sellPage");
        return "/page/sellPage";
    }
}
