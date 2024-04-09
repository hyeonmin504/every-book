package service.tradeservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Order;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.exception.CancelException;
import service.tradeservice.exception.ChangeException;
import service.tradeservice.exception.NotEnoughStockException;
import service.tradeservice.repository.OrderRepository;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RoomService roomService;

    @Transactional
    public void updateStock(Long orderId, int stock) {
        Order getOrder = orderRepository.findById(orderId).orElseThrow();
        Room getRoom = getOrder.getRoom();

        if (!(getOrder.getOrderStatus() == Order.TRADING)){
            throw new ChangeException("확정을 하지 않은 상태에서만 변경이 가능합니다");
        }

        int newStock = stockUpdateValidation(getRoom,stock);
        if (newStock == 0) {
            log.info("newStock={}",newStock);
            roomService.CancelTradeRoom(getRoom.getUser().getId(), getRoom.getId());
            return ;
        }
        getOrder.changeStock(newStock);
    }

    private int stockUpdateValidation(Room room, int compareStock) {
        log.info("stockUpdateValidation start");
        Item getItem = room.getItem();
        if (compareStock == 0) {
            log.info("compareStock=0");
            return 0;
        }
        if (getItem.compareStockQuantity(getItem, compareStock)){
            log.info("compareStock={}",compareStock);
            return compareStock;
        }
        throw new NotEnoughStockException("재고 수량을 참고해서 수량을 변경해주세요");
    }


}
