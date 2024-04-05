package service.tradeservice.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

/**
 * SALE - 판매중 == 보임
 * NO_STOCK - 재고 수량이 없음
 * COMP - 판매 완료 == 수량 감소 + 안보임
 * CANCEL - 판매 취소 == 삭제
 */
public enum RegisterStatus {
    SALE,NO_STOCK,COMP,CANCEL
}
