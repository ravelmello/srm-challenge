package com.ravel.teste.srm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class ExchangeTax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Coin origin;

    @ManyToOne
    private Coin destiny;

    @ManyToOne
    private Product product;

    private BigDecimal tax;

    private LocalDate taxDate;
}
