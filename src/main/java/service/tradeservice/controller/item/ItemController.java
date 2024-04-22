package service.tradeservice.controller.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.NotFoundException;
import service.tradeservice.login.LoginForm;
import service.tradeservice.login.SignUpForm;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ItemService;

import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
@Slf4j
@RequestMapping("/page/items")
public class ItemController {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;


    @GetMapping("/{userId}/category")
    public String sellPage(@PathVariable Long userId, Model model) {
        log.info("getMapping sellPage");
        model.addAttribute("userId", userId);
        return "page/sell/category";
    }

    @GetMapping("/{userId}/item/{category}/{itemId}")
    public String showItem(@PathVariable Long itemId, @PathVariable Category category, Model model) {
        log.info("getMapping showItem");
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();

        BookInfoForm bookInfoForm = new BookInfoForm(item.getId(),item.getItemName(),item.getPrice(),item.getStockQuantity(),seller.getNickName(),item.getCategory(),seller.getSoldItemCount(),seller.getUniversity());

        model.addAttribute("form",bookInfoForm);
        return "page/sell/item";
    }

    @GetMapping("{userId}/{category}")
    public String createBook(@ModelAttribute("bookForm") RegistrationBookForm bookForm,
                             @PathVariable("category") String category,
                             Model model) {

        try {Category cate = Category.valueOf(category);
            bookForm.setCategory(cate);
        } catch (Exception e) {
            return "page/sell/item";
        }

        model.addAttribute("category", RegistrationBookForm.Category());
        return "page/sell/book";
    }

    @PostMapping("{userId}/{category}")
    public String registrationBook(@Validated @ModelAttribute("bookForm") RegistrationBookForm bookForm,
                                   BindingResult bindingResult, Model model,
                                   @PathVariable Long userId, @PathVariable String category){

        if (bookForm.getPrice() != null && bookForm.getStockQuantity() != null){
            int resultPrice = bookForm.getPrice() * bookForm.getStockQuantity();
            if (resultPrice>1000000 ) {
                bindingResult.rejectValue("price",null,null, "가격 * 수량의 합은 0원 이상, 100만원 이하 이어야 합니다. 현재 값 = "+ resultPrice);
            }
        }

        // BindingResult 체크 추가
        if (bindingResult.hasErrors()) {
            log.info("hasErrors");
            // 필요한 모델 속성 추가
            model.addAttribute("userId", userId);
            model.addAttribute("bookForm",bookForm);
            model.addAttribute("category", RegistrationBookForm.Category());

            return "page/sell/book"; // 폼 오류 발생 시 폼 페이지로 이동
        }

        //Book 객체 생성 및 저장
        Book book = new Book(Category.BOOK, bookForm.getItemName(), bookForm.getPrice(), bookForm.getStockQuantity(),bookForm.getAuthor(),bookForm.getPublisher(),bookForm.getPublisherDate(),bookForm.getBookStatus(),bookForm.getWrittenStatus());
        Book savedBook = itemService.registBook(book, userId);

        return "redirect:/page/items/{userId}";
    }

}
