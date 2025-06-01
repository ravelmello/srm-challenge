package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String product;
    private String originCoin;
    private String destinationCoin;
    private String kingdom;
}
