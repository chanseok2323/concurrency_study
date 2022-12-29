package com.chanseok.concurrency.service;

import org.springframework.stereotype.Service;

@Service
public class SynchronizedTransactionProductService {

    private final ProductService productService;

    public SynchronizedTransactionProductService(ProductService productService) {
        this.productService = productService;
    }

    public synchronized void decrease(Long id, Long quantity) {
        productService.decrease(id, quantity);
    }
}
