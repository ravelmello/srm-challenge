package com.ravel.teste.srm.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChangeStrategyFactory {

    private final Map<String, ChangeStrategy> strategies;

    public ChangeStrategyFactory(List<ChangeStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(e->e.getClass().getAnnotation(Component.class).value(), e->e));
    }

    public ChangeStrategy getStrategy(String productName) {
        return strategies.getOrDefault(productName.toUpperCase(), strategies.get("DEFAULT_TAX"));
    }
}

