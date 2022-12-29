package com.chanseok.concurrency.domain;

import javax.persistence.*;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private Long productId;

    private Long quantity;

    @Version
    private Long version;

    public Product() {
    }

    public Product(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void decrease(Long quantity) {
        if(this.quantity - quantity < 0) {
            throw new RuntimeException();
        }
        this.quantity = this.quantity - quantity;
    }
}
