package com.chanseok.concurrency.service;

import com.chanseok.concurrency.domain.Product;
import com.chanseok.concurrency.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SynchronizedProductService {

    private final ProductRepository productRepository;

    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        Product product = productRepository.findById(id).orElseThrow();
        product.decrease(quantity);
        productRepository.saveAndFlush(product);
    }
}
