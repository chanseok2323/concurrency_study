package com.chanseok.concurrency.service;

import com.chanseok.concurrency.domain.Product;
import com.chanseok.concurrency.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SynchronizedProductServiceTest {

    @Autowired
    SynchronizedProductService synchronizedProductService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    public void before() {
        Product product = new Product(1L, 100L);
        productRepository.saveAndFlush(product);
    }

    @AfterEach
    public void after() {
        productRepository.deleteAll();
    }

    @Test
    public void synchronizedWithTransaction() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
               try {
                   synchronizedProductService.decrease(1L, 1L);
               } finally {
                   latch.countDown();
               }
            });
        }

        latch.await();

        Product product = productRepository.findById(1L).orElseThrow();
        assertNotEquals(0L, product.getQuantity());
    }
}