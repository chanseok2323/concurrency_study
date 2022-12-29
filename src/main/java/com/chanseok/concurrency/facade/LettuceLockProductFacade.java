package com.chanseok.concurrency.facade;

import com.chanseok.concurrency.repository.RedisLockRepository;
import com.chanseok.concurrency.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockProductFacade {

    private RedisLockRepository redisLockRepository;

    private ProductService productService;

    public LettuceLockProductFacade(RedisLockRepository redisLockRepository, ProductService productService) {
        this.redisLockRepository = redisLockRepository;
        this.productService = productService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            productService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }
    }
}
