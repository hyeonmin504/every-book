package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Orders;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.domain.item.RegisterStatus;
import service.tradeservice.domain.user.User;
import service.tradeservice.exception.AuthRequitedException;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.ChangeException;
import service.tradeservice.repository.ItemRepository;
import service.tradeservice.repository.OrderRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.UserRepository;

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
    OrderRepository orderRepository;


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
        User buyer = userRepository.findById(buyerId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();

        if (createRoomValidation(buyer,itemId)){
            Orders order = Orders.createOrder(item.getPrice(), orderCount);

            Room room = Room.createRoom(buyer, item, order);
            log.info("방 생성 완료={}", room.getId());

            return roomRepository.save(room);
        }
        throw new CancelException("이미 판매된 상품이어서 방을 생성할 수 없습니다");
    }


    //경우의 수 생각해서 다시 해보고 테스트 코드 작성하
    public void InvisibleRoom(Long requestUser, Long roomId){
        User user = userRepository.findById(requestUser).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        roomDeleteValidation(user, roomId);

        if (room.getItem().getRegisterStatus().equals(RegisterStatus.TRADE)) {
            log.info("구매자(={})가 구매취소를 해서 item 상태가 TRADE에서 ={}로 바뀌었습니다.",user.getNickName(), room.getItem().getRegisterStatus());
            room.getItem().changeRegisterStatus(RegisterStatus.SALE);

        }

        //채팅방이 안보이도록 바꾸기
        room.getOrder().changeOrderStatus(Orders.CANCEL);
        //room.changeState(Room.INVISIBLE);
    }

    public void CancelTrade() {

    }

    public void sellItemConfirm(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (room.getOrder().getOrderStatus() == Orders.CANCEL){
            throw new ChangeException("판매가 취소된 채팅방입니다.");
        }
        if (room.getOrder().getOrderStatus() == Orders.COMP){
            log.info("구매가 성공적으로 끝났습니다");
            return ;
        }
        if (room.getOrder().getOrderStatus() == Orders.TRADE) {
            log.info("이미 판매를 확정했습니다");
        }
        /**
         * true = 구매수량과 판매수량이 같은 경우 현재 item의 판매중을 거래중으로 바꾼다
         * false = 여전히 판매중으로 보여진다
         */
        if (room.getItem().getStockQuantity() == room.getOrder().getQuantity()){
            room.getItem().changeRegisterStatus(RegisterStatus.TRADE);
        }
        room.getOrder().changeOrderStatus(Orders.TRADE);
        log.info("구매자가 판매완료 버튼을 눌렀습니다={}",room.getOrder().getOrderStatus());
    }

    public void buyItemConfirm(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (room.getOrder().getOrderStatus() == Orders.CANCEL) {
            throw new ChangeException("판매가 취소된 채팅방입니다.");
        }
        if (room.getOrder().getOrderStatus() == Orders.SALE){
            log.info("판매자가 먼저 판매 확정 버튼을 눌러야 합니다={}",room.getOrder().getOrderStatus());
            return ;
        }
        if (room.getOrder().getOrderStatus() == Orders.COMP){
            log.info("이미 눌렀습니다");
            return ;
        }
        room.getOrder().changeOrderStatus(Orders.COMP);
        log.info("구매가 성공적으로 끝났습니다={}",room.getOrder().getOrderStatus());

        /**
         * true = 다 팔린 경우 item 상태를 comp로 바꾼다
         * false = 아직 재고 수량이 남아 있어서, SALE 상태 그대로 간다.
         */
        if (room.getItem().removeStock(room.getOrder().getQuantity()) == 0){
            room.getItem().changeRegisterStatus(RegisterStatus.COMP);
        }
        room.getItem().changeRegisterStatus(RegisterStatus.SALE);
    }

    private void roomDeleteValidation(User requestUser, Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();

        if (requestUser.getId().equals(room.getItem().getSellerId()) && room.getOrder().getOrderStatus() == Orders.TRADE){
            throw new CancelException("판매자는 거래중일 때 방을 삭제할 수 없습니다.");
        }
    }

    /**
     * @param buyer 구매자
     * @param itemId 구매하려는 상품
     * @return true - 판매중인 상품
     *          false - 판매 완료된 상품
     */
    private Boolean createRoomValidation(User buyer, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        User seller = userRepository.findById(item.getSellerId()).orElseThrow();
        if (seller.getId().equals(buyer.getId())){
            log.info("판매자 요청={}", seller.getNickName());

        }
        if (seller.getUniversity() != buyer.getUniversity()){
            throw new AuthRequitedException("학교가 서로 다릅니다");
        }
        if (item.getRegisterStatus() == RegisterStatus.SALE){
            log.info("판매중인 상품 확인, 방 생성 가능");
            return true;
        }
        return false;
    }

}
