package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeResponseDTO {
    private BigDecimal convertedAmount;
    private BigDecimal taxRate;
    private String productName;
    private String originCoin;
    private String destinyCoin;
    private LocalDate localDate;
}
