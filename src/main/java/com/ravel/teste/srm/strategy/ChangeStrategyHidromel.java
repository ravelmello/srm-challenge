package com.ravel.teste.srm.strategy;

import com.ravel.teste.srm.entity.ExchangeTax;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import com.ravel.teste.srm.entity.Kingdom;

@Component("HIDROMEL")
public class ChangeStrategyHidromel implements ChangeStrategy {
    @Override
    public BigDecimal change(BigDecimal origin, ExchangeTax tax) {
        Kingdom destiny = tax.getDestiny().getKingdom();

        if("SRM".equalsIgnoreCase(destiny.getKingdomName())){
            BigDecimal convertedValue = origin.multiply(tax.getTax());
            return convertedValue.multiply(new BigDecimal("1.10"));
        }


        return origin.multiply(tax.getTax());
    }
}
