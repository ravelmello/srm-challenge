package com.ravel.teste.srm.strategy;

import com.ravel.teste.srm.entity.ExchangeTax;

import java.math.BigDecimal;

public interface ChangeStrategy {
    BigDecimal change(BigDecimal origin, ExchangeTax tax);
}
