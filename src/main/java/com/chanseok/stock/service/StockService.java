package com.chanseok.stock.service;

import com.chanseok.stock.domain.Stock;
import com.chanseok.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     *  synchronized 를 하더라도 여러 스레드 접근 시 문제 발생
     *  why?
     * @Transactional 은 해당 클래스를 프록시 객체로 만들어서 사용
     * startTansaction -> target 호출 후, endTransaction 시 다른 스레드에서 해당 target을 호출 할 수 있기 때문에 동시성 문제 발생
     */
    //@Transactional
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }
}
