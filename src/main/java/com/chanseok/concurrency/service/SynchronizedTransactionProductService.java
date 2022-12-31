package com.chanseok.concurrency.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SynchronizedTransactionProductService {

    private final ProductService productService;

    public synchronized void decrease(Long id, Long quantity) {
        productService.decrease(id, quantity);
    }
}
