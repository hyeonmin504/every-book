package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Content;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.*;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static service.tradeservice.domain.Room.INVISIBLE;
import static service.tradeservice.domain.Room.VISIBLE;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentService contentService;

    /**
     * @date 2023-19
     * @person hy_min
     * @param buyerId 구매자 아이디
     * @param itemId 구매자가 선택한 아이템
     * @param orderCount 아이템 개수
     * @return room
     */
    @Transactional
    public Room createRoom(Long buyerId, Long itemId, int orderCount){
        log.info("createRoom start");
        User buyer = userRepository.findById(buyerId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();

        Long existRoomId = createRoomValidation(buyer, itemId, orderCount);

        if (existRoomId == null) {
            log.info("existRoomId == null, 방 생성");
            Order order = Order.createOrder(item.getPrice(), orderCount);

            Room room = Room.createRoom(buyer, item, order);
            log.info("방 생성 완료={}", room.getId());

            Room savedRoom = roomRepository.save(room);
            contentService.sendChat(buyerId,room,"대화를 시작해보세요!");

            return savedRoom;
        }
        log.info("안보이던 방을 다시 보이게 합니다");
        return roomRepository.findById(existRoomId).orElseThrow();
    }

    @Transactional
    public List<Room> findAllRoom(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return roomRepository.findByUser(user);
    }


    /**
     * orderStatus
     * ROOM_CREATE - 삭제 가능
     * SELL_CONFIRM -> 판매자만 삭제 불가능
     * TRADE_COMP -> 삭제 가능
     * TRADE_CANCEL -> 안보임
     *
     * RegisterStatus
     * SALE - 변경 사항 없음
     * NO_STOCK -> SELL_CONFIRM + 구매자가 취소하는 경우
     * COMP -> 해당사항 없음 이미 거래된 상태
     * CANCEL -> 해당사항 없음 이미 취소된 상태
     */

    @Transactional
    public void cancelTradeRoom(Long requestUser, Long roomId){
        log.info("cancelTradeRoom start");
        User user = userRepository.findById(requestUser).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        //orderStatus 가 SELL_CONFIRM, TRADE_COMP 판매자인 경우 제외
        log.info("CancelTradeRoom - roomDeleteValidation start");
        roomDeleteValidation(user, roomId);
        log.info("CancelTradeRoom - roomDeleteValidation end");
        //RegisterStatus
        log.info("CancelTradeRoom - RegisterStatus 검증 start");
        if (room.getItem().getRegisterStatus().equals(RegisterStatus.NO_STOCK)) {
            room.getItem().changeRegisterStatus(RegisterStatus.SALE);
            log.info("구매자(={})가 구매취소를 해서 item 상태가 NO_STOCK에서 ={}로 바뀌었습니다.",user.getNickName(), room.getItem().getRegisterStatus());
        }
        log.info("CancelTradeRoom - RegisterStatus 검증 end");
        //채팅방이 안보이도록 바꾸기
        room.getOrder().changeOrderStatus(Order.TRADE_CANCEL);
        room.getOrder().soldDate(LocalDateTime.now());
        room.changeState(Room.INVISIBLE);
        log.info("CancelTradeRoom end");
    }

    @Transactional
    public void sellItemConfirm(Long roomId) {
        log.info("sellItemConfirm start");
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (room.getItem().getStockQuantity() < room.getOrder().getStock()){
            throw new NotEnoughStockException("need more stock");
        }
        if (room.getOrder().getOrderStatus() == Order.TRADE_CANCEL){
            throw new ChangeException("판매가 취소된 채팅방입니다.");
        }
        if (room.getOrder().getOrderStatus() == Order.TRADE_COMP){
            log.info("판매가 성공적으로 끝났습니다");
            throw new CompleteTradeException("판매가 성공적으로 끝났습니다");
        }
        if (room.getOrder().getOrderStatus() == Order.SELL_CONFIRM) {
            log.info("이미 판매를 확정했습니다");
            throw new ConfirmException("이미 판매를 확정했습니다");
        }
        /**
         * true = 구매수량과 판매수량이 같은 경우 현재 item의 판매중을 NO_STOCK으로 바꾼다
         * false = 여전히 판매중으로 보여진다
         */
        if (room.getItem().getStockQuantity() == room.getOrder().getStock()){
            room.getItem().changeRegisterStatus(RegisterStatus.NO_STOCK);
            log.info(".NO_STOCK, 재고가 없음 + 판매 확정을 하면서 no_stock변경");
        }
        room.getOrder().changeOrderStatus(Order.SELL_CONFIRM);
        log.info("판매자가 판매확정 버튼을 눌렀습니다={}",room.getOrder().getOrderStatus());
        log.info("sellItemConfirm end");
    }

    @Transactional
    public void buyItemConfirm(Long roomId) {
        log.info("buyItemConfirm start");
        Room room = roomRepository.findById(roomId).orElseThrow();
        room.getItem().removeStock(room.getOrder().getStock());
        if (room.getOrder().getOrderStatus() == Order.TRADE_CANCEL) {
            throw new ChangeException("판매가 취소된 채팅방입니다.");
        }
        if (room.getOrder().getOrderStatus() == Order.TRADING){
            log.info("판매자가 먼저 판매 확정 버튼을 눌러야 합니다={}",room.getOrder().getOrderStatus());
            throw new SequenceException("판매자가 먼저 판매 확정 버튼을 눌러야 합니다");
        }
        if (room.getOrder().getOrderStatus() == Order.TRADE_COMP){
            log.info("이미 구매확정 버튼을 눌렀습니다");
            throw new ConfirmException("이미 구매확정 버튼을 눌렀습니다");
        }

        room.getOrder().changeOrderStatus(Order.TRADE_COMP);
        log.info("채팅방 상태를 거래 완료로 변경");
        /**
         * true = 재고가 없는 경우 item 상태를 comp로 바꾼다
         * false = 아직 재고 수량이 남아 있어서, SALE 상태 그대로 간다.
         */
        if (room.getItem().getRegisterStatus().equals(RegisterStatus.NO_STOCK)){
            room.getItem().changeRegisterStatus(RegisterStatus.COMP);
            log.info("상품의 상태를 판매 완료로 변경");
            return ;
        }
        room.getItem().changeRegisterStatus(RegisterStatus.SALE);

        log.info("판매자 판매 횟수 증가");
        User seller = userRepository.findById(room.getItem().getSellerId()).orElseThrow();
        seller.changeSoldItemCount();

        log.info("구매가 성공적으로 끝났습니다={}, 상품의 상태는 판매중",room.getOrder().getOrderStatus());
    }

    private void roomDeleteValidation(User requestUser, Long roomId) {
        log.info("roomDeleteValidation start");
        Room room = roomRepository.findById(roomId).orElseThrow();

        // 판매자가 판매확정을 누른 상태에서 삭제 요청을 할 때
        if (requestUser.getId().equals(room.getItem().getSellerId()) && room.getOrder().getOrderStatus() == Order.SELL_CONFIRM){
            throw new CancelException("판매자는 거래중일 때 방을 삭제할 수 없습니다.");
        } else if (room.getOrder().getOrderStatus() == Order.TRADE_COMP) {
            throw new CompleteTradeException("거래가 완료상태일 때 방을 삭제할 수 없습니다.");
        }
        log.info("roomDeleteValidation end 문제 없음");
    }

    /**
     * @param buyer 구매자
     * @param itemId 구매하려는 상품
     * @return true - 판매중인 상품
     *          false - 판매 완료된 상품
     */
    private Long createRoomValidation(User buyer, Long itemId, int orderCount) {
        log.info("createRoomValidation start");
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();

        //방의 존재 유무
        if (seller.getId().equals(buyer.getId())){
            log.info("판매자 요청={}", seller.getNickName());
            throw new AuthRequitedException("판매자와 구매자가 같을 수 없습니다");
        }
        if (seller.getUniversity() != buyer.getUniversity()){
            throw new AuthRequitedException("학교가 서로 다릅니다");
        }
        if (item.getStockQuantity() < orderCount){
            throw new NotEnoughStockException("need more stock");
        }

        List<Room> existRoom = roomRepository.findSameRoom(buyer, item, orderCount);
        if (item.getRegisterStatus() == RegisterStatus.SALE){
            log.info("상품 판매중 확인");
            if (!existRoom.isEmpty()){
                for (Room room : existRoom) {
                    log.info("방을 만들다가 이미 존재하는 방을 찾음");
                    if (room.getState()==INVISIBLE){
                        log.info("방 상태가 안보이다가 다시 보이도록 바꾼다 + ");
                        room.changeState(VISIBLE);
                        room.getOrder().changeOrderStatus(Order.TRADING);
                        return room.getId();
                    }
                }
                throw new AuthRequitedException("이미 방이 존재합니다");
            }
            log.info("판매중인 상품 확인, 방 생성 가능");
            return null;
        }
        throw new CancelException("이미 판매된 상품이어서 방을 생성할 수 없습니다");
    }

    public List<Room> findAllSellerRoom(Long userId) {
        return roomRepository.findBySellerRoom(userId);
    }
}
