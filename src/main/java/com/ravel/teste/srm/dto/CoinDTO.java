package com.ravel.teste.srm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinDTO {
   private String nameCoin;
   private String kingdom;
   private String symbol;
}
