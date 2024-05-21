package service.tradeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.tradeservice.domain.Ddd;
import service.tradeservice.exception.DddException;
import service.tradeservice.repository.DataRepository;

@Service
@Slf4j
public class DataService {
    @Autowired
    DataRepository dataRepository;

    public Ddd join (Ddd data) {
        return dataRepository.save(data);
    }

    @Transactional
    public void ex(Ddd data) throws DddException {
        join(data);
        log.info("데이타 프로세스 진입");
        if (data.getPrice() == 1) {
            log.info("시스템 예외");
            throw new RuntimeException("시스템 예외 발생");
        } else if (data.getPrice() == 2) {
            log.info("2 비즈니스 예외 발생");
            data.setPrice(0);
            throw new DddException("숫자가 2입니다");
        } else
            log.info("정상승인");

    }
}
