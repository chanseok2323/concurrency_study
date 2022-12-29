package com.chanseok.concurrency.repository;

import com.chanseok.concurrency.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;


public interface ProductRepository extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Product p where p.no = :no")
    Product findByIdWithinPessimisticLock(Long no);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select p from Product p where p.no = :no")
    Product findByIdWithinOptimisticLock(Long no);

}
