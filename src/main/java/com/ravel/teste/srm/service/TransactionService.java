package com.ravel.teste.srm.service;

import com.ravel.teste.srm.dto.TransactionDTO;
import com.ravel.teste.srm.dto.TransactionResponseDTO;
import com.ravel.teste.srm.entity.*;
import com.ravel.teste.srm.repository.TransactionRepository;
import com.ravel.teste.srm.strategy.ChangeStrategy;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private final ExchangeTaxService exchangeTaxService;
    private final ProductService productService;
    private final CoinService coinService;
    private final KingdomService kingdomService;
    private final TransactionRepository transactionRepository;
    private final Map<String, ChangeStrategy> strategyMap;


    public TransactionService(
            ExchangeTaxService exchangeTaxService,
            ProductService productService,
            CoinService coinService, KingdomService kingdomService,
            TransactionRepository transactionRepository,
            Map<String, ChangeStrategy> strategyMap) {
        this.exchangeTaxService = exchangeTaxService;
        this.productService = productService;
        this.coinService = coinService;
        this.kingdomService = kingdomService;
        this.transactionRepository = transactionRepository;
        this.strategyMap = strategyMap;

    }


    @Retryable(
            exceptionExpression = "#{@retryExceptionEvaluator.isRetryableException(#root.cause)}",
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {

        Product product = getProductByName(transactionDTO.getProduct());
        ChangeStrategy strategy = selectStrategy(product);
        Coin originCoin = searchCoin(transactionDTO.getOriginCoin());
        Coin destinyCoin = searchCoin(transactionDTO.getDestinationCoin());

        Kingdom kingdom = searchKingdom(transactionDTO.getKingdom());

        ExchangeTax exchangeTax = searchTaxForProduct(product.getId(), originCoin.getName(), destinyCoin.getName());

        BigDecimal convertedAmount = strategy.change(product.getBasePrice(), exchangeTax);

        Transaction newTransaction = new Transaction();
        newTransaction.setProduct(product);
        newTransaction.setOriginCoin(originCoin);
        newTransaction.setKingdom(kingdom);
        newTransaction.setDestinyCoin(destinyCoin);
        newTransaction.setExchangeTax(exchangeTax);
        newTransaction.setAmountOrigin(product.getBasePrice());
        newTransaction.setAmountDestiny(convertedAmount);
        newTransaction.setTransactionDateTime(LocalDateTime.now());

        transactionRepository.save(newTransaction);
        LOGGER.info("New transaction created: {}", newTransaction);

        return new TransactionResponseDTO(
                product.getName(),
                product.getBasePrice(),
                exchangeTax.getTax(),
                convertedAmount,
                kingdom.getKingdomName(),
                originCoin.getName(),
                destinyCoin.getName(),
                newTransaction.getTransactionDateTime().toLocalDate()
        );
    }


    public TransactionResponseDTO toDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setProductName(transaction.getProduct().getName());
        dto.setProductBasePrice(transaction.getProduct().getBasePrice());
        dto.setTaxRate(transaction.getExchangeTax().getTax());
        dto.setConvertedAmount(transaction.getAmountDestiny());
        dto.setKingdomOfTransaction(transaction.getKingdom().getKingdomName());
        dto.setOriginCoin(transaction.getOriginCoin().getName());
        dto.setDestinyCoin(transaction.getDestinyCoin().getName());
        dto.setLocalDate(transaction.getTransactionDateTime().toLocalDate());

        return dto;
    }

    public Page<TransactionResponseDTO> toDtoList(Page<Transaction> transactions) {
        List<TransactionResponseDTO> dtoList = transactions.stream().map(this::toDTO).toList();
        return new PageImpl<>(dtoList, transactions.getPageable(), transactions.getTotalElements());
    }


    public Page<Transaction> findAllPageabled(int page, int lenght){
        return transactionRepository.findAll(PageRequest.of(page, lenght));
    }

    private Kingdom searchKingdom(String kingdom) {
        return kingdomService.getKingdomByName(kingdom);
    }

    public ExchangeTax searchTaxForProduct(Long productID, String originCoin, String destinyCoin) {
        return exchangeTaxService.getLatestTax(productID, originCoin, destinyCoin);
    }

    private Coin searchCoin(String coinName) {
       return coinService.getCoinByName(coinName);
    }

    private Product getProductByName(String productID) {
        return productService.findByName(productID);
    }

    public   ChangeStrategy selectStrategy(Product product) {
        return strategyMap.getOrDefault(product.getName().toUpperCase(), strategyMap.get("DEFAULT_TAX"));
    }



}
