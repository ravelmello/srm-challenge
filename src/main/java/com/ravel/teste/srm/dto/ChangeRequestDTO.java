package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestDTO {
    private Long productID;
    private BigDecimal amount;
    private String originCoin;
    private String destinyCoin;
}
