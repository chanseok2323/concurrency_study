package com.chanseok.concurrency.service;

import com.chanseok.concurrency.domain.Product;
import com.chanseok.concurrency.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SynchronizedProductService {

    private final ProductRepository productRepository;

    public SynchronizedProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        Product product = productRepository.findById(id).orElseThrow();
        product.decrease(quantity);
        productRepository.saveAndFlush(product);
    }
}
