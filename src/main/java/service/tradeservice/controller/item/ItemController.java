package service.tradeservice.controller.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.tradeservice.controller.item.book.BookInfoForm;
import service.tradeservice.controller.item.book.EditBookForm;
import service.tradeservice.controller.item.book.RegistrationBookForm;
import service.tradeservice.controller.room.CreateRoomForm;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.Category;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.user.User;
import service.tradeservice.login.LoginForm;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;
import service.tradeservice.service.ItemService;
import service.tradeservice.service.RoomService;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    RoomService roomService;

    @GetMapping("/{userId}")
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

    @GetMapping("/{userId}/category")
    public String sellPage(@PathVariable Long userId, Model model) {
        log.info("getMapping sellPage");
        model.addAttribute("userId", userId);
        return "page/sell/category";
    }

    @GetMapping("/{userId}/item/{category}/{itemId}")
    public String showItem(@PathVariable Long itemId, @PathVariable Category category,
                           @PathVariable Long userId, Model model,
                           @ModelAttribute BookInfoForm bookInfoForm,
                           @ModelAttribute CreateRoomForm roomForm) {
        log.info("getMapping showItem");
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();
        bookInfoForm = new BookInfoForm(item.getId(),item.getItemName(),item.getPrice(),item.getStockQuantity(),seller.getNickName(),item.getCategory(),seller.getSoldItemCount(),seller.getUniversity());

        model.addAttribute("userId",userId);
        model.addAttribute("itemId",itemId);
        model.addAttribute("form",bookInfoForm);
        model.addAttribute("roomForm",roomForm);
        return "page/sell/item";
    }

    /**
     * item 페이지에서 구매수량 선택 후 -> roomController 채팅방 만들기
     * -> 채팅 창으로 이동
     */
    @PostMapping("/{userId}/item/{category}/{itemId}")
    public String createRoom(@PathVariable Long userId,@PathVariable Long itemId,
                             @ModelAttribute BookInfoForm bookInfoForm,
                             @ModelAttribute CreateRoomForm roomForm,Model model,
                             BindingResult bindingResult,RedirectAttributes redirectAttributes) {
        //Room.createRoom()
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        bookInfoForm = new BookInfoForm(item.getId(),item.getItemName(),item.getPrice(),item.getStockQuantity(),seller.getNickName(),item.getCategory(),seller.getSoldItemCount(),seller.getUniversity());

        if (!(roomForm.getStock()>0 && roomForm.getStock()<=item.getStockQuantity())){
            bindingResult.rejectValue("stock",null,null, "수량 선택 오류. 재고 수량 = "+ item.getStockQuantity());
        }

        if (bindingResult.hasErrors()) {
            log.info("hasErrors");
            // 필요한 모델 속성 추가
            model.addAttribute("userId",userId);
            model.addAttribute("itemId",itemId);
            model.addAttribute("form",bookInfoForm);
            model.addAttribute("roomForm",roomForm);

            return "page/sell/item";
        }
        try{
            // room:null = 방 존재 x , room:null x : 1. 안보이는 방 2.이미 존재하는 방
            Room room = roomService.createRoom(user.getId(), item.getId(), roomForm.getStock());
            log.info("room={}",room.getId());
            redirectAttributes.addAttribute("roomId", room.getId());
            return "redirect:/page/roomPage/{userId}/chatPage/{roomId}";
        } catch (Exception e ) {
            log.info("오류 발생",e);
            model.addAttribute("userId",userId);
            model.addAttribute("itemId",itemId);
            model.addAttribute("form",bookInfoForm);
            model.addAttribute("roomForm",roomForm);
            return "page/sell/item";
        }
    }

    @GetMapping("/{userId}/item/{category}/{itemId}/info")
    public String itemInfo(@ModelAttribute("bookForm") EditBookForm bookForm,
                             @PathVariable String category,
                             @PathVariable Long userId,
                             @PathVariable Long itemId,
                             Model model) {

        if (category.equals(Category.BOOK.toString())) {
            Book book = (Book) itemRepository.findById(itemId).orElseThrow();
            bookForm = new EditBookForm(book.getStockQuantity(),Category.BOOK,book.getAuthor(),book.getPublisher(),book.getPublisherDate(),book.getBookStatus(),book.getWrittenStatus());
            model.addAttribute("category", RegistrationBookForm.Category());
            model.addAttribute("bookForm", bookForm);
            log.info("itemName={}",book.getItemName());
            model.addAttribute("itemName", book.getItemName());
            model.addAttribute("price", book.getPrice());
        }
        model.addAttribute("userId",userId);
        model.addAttribute("itemId",itemId);

        return "page/sell/itemInfo";
    }

    @GetMapping("/{userId}/item/{category}/{itemId}/edit")
    public String editItem(@ModelAttribute EditBookForm bookForm,
                           @PathVariable String category,
                           @PathVariable Long userId,
                           @PathVariable Long itemId,
                           Model model) {

        if (category.equals(Category.BOOK.toString())) {
            Book item = (Book) itemRepository.findById(itemId).orElseThrow();
            bookForm = new EditBookForm(item.getStockQuantity(),item.getCategory(),item.getAuthor(),item.getPublisher(),item.getPublisherDate(),item.getBookStatus(),item.getWrittenStatus());
            model.addAttribute("category", RegistrationBookForm.Category());
            model.addAttribute("bookForm", bookForm);
            model.addAttribute("itemName", item.getItemName());
            model.addAttribute("price", item.getPrice());
        }
        model.addAttribute("bookForm", bookForm);
        model.addAttribute("userId",userId);

        return "page/sell/edit";
    }

    @PostMapping("/{userId}/item/{category}/{itemId}/edit")
    public String saveItem(@Validated @ModelAttribute EditBookForm bookForm,
                           BindingResult bindingResult,
                           @PathVariable String category,
                           @PathVariable Long userId,
                           @PathVariable Long itemId,
                           Model model) {

        if (bookForm.getStockQuantity() != null) {
            if (!(bookForm.getBookStatus() > 0 || bookForm.getStockQuantity() <= 30)) {
                bindingResult.rejectValue("stockQuantity",null,null, "수량은 0~30 사이에서 정해주세요");
            }
        }

        if (bindingResult.hasErrors()) {
            log.info("hasErrors");
            // 필요한 모델 속성 추가
            Item item = itemRepository.findById(itemId).orElseThrow();
            model.addAttribute("userId", userId);
            model.addAttribute("bookForm",bookForm);
            model.addAttribute("category", RegistrationBookForm.Category());
            model.addAttribute("itemName", item.getItemName());
            model.addAttribute("price", item.getPrice());
            return "page/sell/edit"; // 폼 오류 발생 시 폼 페이지로 이동
        }

        Book book = new Book(bookForm.getStockQuantity(),bookForm.getAuthor(),bookForm.getPublisher(),bookForm.getPublisherDate(),bookForm.getBookStatus(),bookForm.getWrittenStatus());
        itemService.updateBook(book,itemId,userId);
        return "redirect:/page/items/{userId}/item/{category}/{itemId}/info";
    }

    @GetMapping("{userId}/{category}")
    public String createBook(@ModelAttribute("bookForm") RegistrationBookForm bookForm,
                             @PathVariable("category") String category,
                             @PathVariable("userId") Long userId,
                             Model model) {

        try {Category cate = Category.valueOf(category);
            bookForm.setCategory(cate);
        } catch (Exception e) {
            return "page/sell/item";
        }

        model.addAttribute("category", RegistrationBookForm.Category());
        model.addAttribute("userId",userId);
        return "page/sell/book";
    }

    @PostMapping("{userId}/{category}")
    public String registrationBook(@Validated @ModelAttribute("bookForm") RegistrationBookForm bookForm,
                                   BindingResult bindingResult, Model model,
                                   @PathVariable Long userId, @PathVariable String category) {

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
            model.addAttribute("bookForm", bookForm);
            model.addAttribute("category", RegistrationBookForm.Category());

            return "page/sell/book"; // 폼 오류 발생 시 폼 페이지로 이동
        }

        //Book 객체 생성 및 저장
        Book book = new Book(Category.BOOK, bookForm.getItemName(), bookForm.getPrice(), bookForm.getStockQuantity(), bookForm.getAuthor(), bookForm.getPublisher(), bookForm.getPublisherDate(), bookForm.getBookStatus(), bookForm.getWrittenStatus());
        Book savedBook = itemService.registBook(book, userId);

        return "redirect:/page/items/{userId}";
    }
}
