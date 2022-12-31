package com.chanseok.concurrency.facade;

import com.chanseok.concurrency.repository.RedisLockRepository;
import com.chanseok.concurrency.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LettuceLockProductFacade {
    private final RedisLockRepository redisLockRepository;

    private final ProductService productService;

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
