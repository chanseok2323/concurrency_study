package com.chanseok.concurrency.service;

import com.chanseok.concurrency.domain.Product;
import com.chanseok.concurrency.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticLockProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Product product = productRepository.findByIdWithinPessimisticLock(id);

        product.decrease(quantity);

        productRepository.saveAndFlush(product);
    }
}
