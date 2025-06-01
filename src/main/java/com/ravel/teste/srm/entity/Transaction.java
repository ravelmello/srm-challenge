package com.ravel.teste.srm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private Kingdom kingdom;

    @ManyToOne
    private ExchangeTax exchangeTax;

    private BigDecimal amountOrigin;
    private BigDecimal amountDestiny;

    private LocalDateTime transactionDateTime;
}
