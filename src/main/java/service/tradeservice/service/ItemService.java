package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.item.Book;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;


    @Transactional
    public Book registBook(Book book, Long userId) {
        addValidationBook(book, userId);
        itemRepository.save(book);
        book.setRegisteredItemDate();
        log.info("registered item={}", book.getId());
        return book;
    }

    @Transactional
    public Book updateBook(Book newBook, Long bookId, Long userId) {
        Book findItem = (Book)itemRepository.findById(bookId).orElseThrow();
        addValidationBook(newBook,userId);
        return Book.updateBook(newBook, findItem);
    }

    @Transactional
    public void cancelBook(Long bookId,Long userId) {
        Book findBook = (Book) itemRepository.findById(bookId).orElseThrow();
        cancelValidationBook(findBook,userId);
        findBook.changeRegisterStatus(RegisterStatus.CANCEL);
    }

    /**
     * item의 등록 취소(CANCEL) 조건
     * 1. registerStatus.SALE - 등록 취소가 가능하다 (SALE -> CANCEL)
     * 2.
     */
    private void cancelValidationBook(Book book, Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow();

        if (!(book.getSellerId().equals(userId))){
            throw new AuthRequitedException("수정 권한이 없습니다");
        }
        if (!(book.getRegisterStatus()== RegisterStatus.SALE)){
            throw new CancelException("판매중인 상태에서만 등록취소가 가능 합니다. 현재 상태 = "+ book.getRegisterStatus());
        }


    }

    /**
     * 책 가격은 최소 0원 무료 나눔부터 1,000,000원 사이에서 결정된다.
     * 재고 수량은 최소 1권부터 30권 사이에서 결정된다.
     */
    private void addValidationBook(Book book, Long userId) {
        int price = book.getPrice();
        int sq = book.getStockQuantity();
        int bookStatus = book.getBookStatus();
        int writtenStatus = book.getWrittenStatus();

        //기존 회원이 맞는지 확인
        User findUser = userRepository.findById(userId).orElseThrow();

        //아이템 첫 판매
        if (book.getSellerId()==null){
            book.setSeller(findUser.getId());
        } else if (!(book.getSellerId().equals(userId))) {
            throw new AuthRequitedException("수정 권한이 없습니다.");
        }

        if (!(bookStatus >= 0 && bookStatus <= 4)){
            throw new AuthRequitedException("책 상태 값을 0~4사이의 값 입니다");
        }
        if (!(writtenStatus >= 0 && writtenStatus <= 4)){
            throw new AuthRequitedException("필기 상태 값을 0~4사이의 값 입니다");
        }
        if (!(price >= 0 && price <= 1000000)) {
            throw new AuthRequitedException("가격의 값은 0원에서 최대 100만원까지 가능합니다");
        }
        if (!(sq > 0 && sq <= 30)){
            throw new AuthRequitedException("수량은 최소 1권에서 최대 30권까지만 등록 가능합니다.");
        }
    }
}
