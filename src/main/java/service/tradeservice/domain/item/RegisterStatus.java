package service.tradeservice.domain.item;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

/**
 * SALE - 판매중 == 보임
 * TRADE - 거래 중 == 안보임
 * COMP - 판매 완료 == 수량 감소 + 안보임
 * CANCEL - 판매 취소 == 삭제
 */
public enum RegisterStatus {
    SALE,TRADE,COMP,CANCEL
}
