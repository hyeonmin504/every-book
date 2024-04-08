package service.tradeservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Orders;
import service.tradeservice.domain.Room;
import service.tradeservice.domain.item.Item;
import service.tradeservice.exception.CancelException;
import service.tradeservice.repository.OrderRepository;
import service.tradeservice.repository.RoomRepository;
import service.tradeservice.repository.impl.OrderRepositoryCustom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RoomService RoomService;

    @Transactional
    public void updateStock(Long orderId, int stock) {
        Orders getOrder = orderRepository.findById(orderId).orElseThrow();
        Room getRoom = getOrder.getRoom();


        int newStock = stockUpdateValidation(getRoom,stock);
        if (newStock == 0) {
            RoomService.CancelTradeRoom(getRoom.getUser().getId(), getRoom.getId());
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
        throw new CancelException("재고 수량을 참고하고 구매 수량을 정해주세요");
    }


}
