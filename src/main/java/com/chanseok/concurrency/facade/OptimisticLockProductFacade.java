package com.chanseok.concurrency.facade;

import com.chanseok.concurrency.service.OptimisticLockProductService;
import org.springframework.stereotype.Service;

@Service
public class OptimisticLockProductFacade {

    private OptimisticLockProductService optimisticLockProductService;

    public OptimisticLockProductFacade(OptimisticLockProductService optimisticLockProductService) {
        this.optimisticLockProductService = optimisticLockProductService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockProductService.decrease(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(50);
            }
        }
    }
}
