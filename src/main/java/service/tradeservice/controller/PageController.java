package service.tradeservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.controller.item.RegistrationItemForm;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.University;
import service.tradeservice.domain.user.User;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/page")
public class PageController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    @GetMapping(value = "/main/{userId}")
    public String loginMainPage(@SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm, Model model,
                                @PathVariable Long userId) {
        log.info("login/main");

        if (loginForm == null) {
            log.info("loginUser==null");
            return "/";
        }

        University buyerUniv = userRepository.findById(userId).orElseThrow().getUniversity();
        List<Item> all = itemRepository.findAllByUniv(buyerUniv);

        List<RegistrationItemForm> itemForm = new ArrayList<>();
        for (Item item : all) {
            User user = userRepository.findById(item.getSellerId()).orElseThrow();
            itemForm.add(new RegistrationItemForm(item.getId(),item.getItemName(),item.getPrice(),item.getStockQuantity(),user.getNickName(),item.getCategory()));
        }

        //세션 유지
        model.addAttribute("user", loginForm);
        model.addAttribute("userId",userId);
        model.addAttribute("items",itemForm);
        return "/page/main";
    }

    @PostMapping(value = "/main/{userId}")
    public String mainPage() {
        return "redirect:/page/main/{userId}";
    }

    @GetMapping("/myPage/{userId}")
    public String myPage() {
        log.info("getMapping myPage");
        return "/page/myPage";
    }

    @GetMapping("/roomPage/{userId}")
    public String roomPage() {
        log.info("getMapping roomPage");
        return "/page/roomPage";
    }

    @GetMapping("/items/{userId}")
    public String Items(@PathVariable Long userId, @SessionAttribute(name = "LOGIN_MEMBER",required = false) LoginForm loginForm,
                        Model model) {
        model.addAttribute("userId", userId);

        List<Item> all = itemRepository.findMyItem(userId);

        List<RegistrationItemForm> itemForm = new ArrayList<>();
        for (Item item : all) {
            User user = userRepository.findById(userId).orElseThrow();
            itemForm.add(new RegistrationItemForm(item.getId(),item.getItemName(),item.getPrice(),item.getStockQuantity(),user.getNickName(),item.getCategory()));
        }
        model.addAttribute("user", loginForm);
        model.addAttribute("items",itemForm);
        return "/page/sell/sellPage";
    }
}