package com.ravel.teste.srm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Coin originCoin;

    @ManyToOne
    private Coin destinyCoin;

    private BigDecimal amountOrigin;
    private BigDecimal amountDestiny;

    private LocalDateTime transactionDateTime;
}
