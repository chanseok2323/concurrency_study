package com.chanseok.stock.repository;

import com.chanseok.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;


public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select s from Stock s where s.id = :id")
    Stock findByIdWithinPessimisticLock(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "select s from Stock s where s.id = :id")
    Stock findByIdWithinOptimisticLock(Long id);

}
