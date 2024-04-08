package service.tradeservice.repository.impl;

import service.tradeservice.domain.Content;

public interface ContentRepositoryCustom {

    public Content findSendInfo(Long userId, Long roomId);
}
