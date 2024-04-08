package service.tradeservice.repository.impl;

import service.tradeservice.domain.Content;

import java.util.List;

public interface ContentRepositoryCustom {

    public List<Content> findSendChat(Long roomId);
}
