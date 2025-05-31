package com.ravel.teste.srm.service;

import com.ravel.teste.srm.dto.ChangeRequestDTO;
import com.ravel.teste.srm.dto.ChangeResponseDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.repository.ExchangeTaxRepository;
import com.ravel.teste.srm.repository.ProductRepository;
import com.ravel.teste.srm.repository.TransactionRepository;
import com.ravel.teste.srm.strategy.ChangeStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ChangeService {
    private final ExchangeTaxRepository exchangeTaxRepository;
    private final ProductRepository productRepository;
    private final CoinRepository coinRepository;
    private final TransactionRepository transactionRepository;
    private Map<String, ChangeStrategy> strategyMap;

    public ChangeService(ExchangeTaxRepository exchangeTaxRepository,
                         ProductRepository productRepository,
                         CoinRepository coinRepository,
                         TransactionRepository transactionRepository,
                         Map<String, ChangeStrategy> strategyMap) {
        this.exchangeTaxRepository = exchangeTaxRepository;
        this.productRepository = productRepository;
        this.coinRepository = coinRepository;
        this.transactionRepository = transactionRepository;
        this.strategyMap = strategyMap;
    }

    @Transactional
    public ChangeResponseDTO calculateProductValueOnDestiny(ChangeRequestDTO requestDTO) {

        Product product = getProductByID(requestDTO.getProductID());
        ChangeStrategy strategy = selectStrategy(product);
       // Coin originCoin =

        return new ChangeResponseDTO();
    }


    private Product getProductByID(Long productID) {
        return productRepository.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productID));
    }
    private ChangeStrategy selectStrategy(Product product) {
        return strategyMap.getOrDefault(product.getName().toUpperCase(), strategyMap.get("DEFAULT"));
    }


}
