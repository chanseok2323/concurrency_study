package com.chanseok.concurrency.facade;

import com.chanseok.concurrency.service.ProductService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockProductFacade {

    private RedissonClient redissonClient;
    private ProductService productService;

    public RedissonLockProductFacade(RedissonClient redissonClient, ProductService productService) {
        this.redissonClient = redissonClient;
        this.productService = productService;
    }

    public void decrease(Long key, Long quantity) {
        RLock lock = redissonClient.getLock(key.toString());

        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if(!available) {
                System.out.println("락 획득 실패");
                return;
            }

            productService.decrease(key, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
