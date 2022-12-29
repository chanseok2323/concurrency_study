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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

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
    public void 일반적인_서비스에서_동시에_접근() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
               try {
                   productService.decrease(1L, 1L);
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