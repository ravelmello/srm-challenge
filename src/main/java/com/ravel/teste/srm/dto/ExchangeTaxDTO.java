package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeTaxDTO {
    private String originCoinName;
    private String destinationCoinName;
    private String productName;
    private BigDecimal taxRate;
}
