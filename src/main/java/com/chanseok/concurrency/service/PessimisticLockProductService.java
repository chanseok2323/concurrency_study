package com.chanseok.concurrency.service;

import com.chanseok.concurrency.domain.Product;
import com.chanseok.concurrency.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessimisticLockProductService {

    private final ProductRepository productRepository;

    public PessimisticLockProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        Product product = productRepository.findByIdWithinPessimisticLock(id);

        product.decrease(quantity);

        productRepository.saveAndFlush(product);
    }
}
