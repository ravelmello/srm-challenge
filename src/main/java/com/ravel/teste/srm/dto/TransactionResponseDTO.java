package com.ravel.teste.srm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productBasePrice")
    private BigDecimal productBasePrice;

    @JsonProperty("taxRate")
    private BigDecimal taxRate;

    @JsonProperty("convertedAmount")
    private BigDecimal convertedAmount;

    @JsonProperty("kingdomOfTransaction")
    private String kingdomOfTransaction;

    @JsonProperty("originCoin")
    private String originCoin;

    @JsonProperty("destinyCoin")
    private String destinyCoin;

    @JsonProperty("localDate")
    private LocalDate localDate;
}
