package com.ravel.teste.srm.strategy;

import com.ravel.teste.srm.entity.ExchangeTax;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("DEFAULT_TAX")
public class ChangeStrategyStandard implements ChangeStrategy{
    @Override
    public BigDecimal change(BigDecimal origin, ExchangeTax tax) {
        return origin.multiply(tax.getTax());
    }
}
