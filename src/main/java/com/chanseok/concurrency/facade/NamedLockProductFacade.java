package com.chanseok.concurrency.facade;

import com.chanseok.concurrency.repository.LockRepository;
import com.chanseok.concurrency.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamedLockProductFacade {
    private final LockRepository lockRepository;

    private final ProductService productService;


    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            productService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
