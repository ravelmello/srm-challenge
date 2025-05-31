package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeRateDTO {
    private Long productID;
    private String originCoin;
    private String destinyCoin;
    private Double rate;
    private LocalDate localDate;
}
