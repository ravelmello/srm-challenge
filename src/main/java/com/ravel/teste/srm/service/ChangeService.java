package com.ravel.teste.srm.service;

import com.ravel.teste.srm.dto.ChangeRequestDTO;
import com.ravel.teste.srm.dto.ChangeResponseDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.ExchangeTax;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.repository.ExchangeTaxRepository;
import com.ravel.teste.srm.repository.ProductRepository;
import com.ravel.teste.srm.repository.TransactionRepository;
import com.ravel.teste.srm.strategy.ChangeStrategy;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
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


    public ChangeResponseDTO calculateProductValueOnDestiny(ChangeRequestDTO requestDTO) {

        Product product = getProductByID(requestDTO.getProductID());
        ChangeStrategy strategy = selectStrategy(product);
        Coin originCoin = searchCoinOrigin(requestDTO.getOriginCoin());
        Coin destinyCoin = searchCoinOrigin(requestDTO.getDestinyCoin());
        ExchangeTax exchangeTax = searchTaxForProduct(requestDTO.getProductID(), requestDTO.getOriginCoin(), requestDTO.getDestinyCoin());

        BigDecimal convertedAmount = strategy.change(requestDTO.getAmount(), exchangeTax);

        return new ChangeResponseDTO(
            convertedAmount,
                exchangeTax.getTax(),
                product.getName(),
                originCoin.getName(),
                destinyCoin.getName(),
                LocalDate.now()
        );
    }

    private ExchangeTax searchTaxForProduct(Long productID, String originCoin, String destinyCoin) {
        return exchangeTaxRepository.searchLastTaxByProduct(productID, originCoin, destinyCoin).orElseThrow(
                () -> new IllegalArgumentException("Not found tax for product " + productID)
        );
    }

    private Coin searchCoinOrigin(String originCoin) {
       return null;
    }

    private Product getProductByID(Long productID) {
        return productRepository.findById(productID)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productID));
    }
    private ChangeStrategy selectStrategy(Product product) {
        return strategyMap.getOrDefault(product.getName().toUpperCase(), strategyMap.get("DEFAULT"));
    }


}
