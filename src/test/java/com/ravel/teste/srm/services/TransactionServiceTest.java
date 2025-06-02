package com.ravel.teste.srm.services;

import com.ravel.teste.srm.dto.TransactionDTO;
import com.ravel.teste.srm.dto.TransactionResponseDTO;
import com.ravel.teste.srm.entity.*;
import com.ravel.teste.srm.repository.TransactionRepository;
import com.ravel.teste.srm.service.*;
import com.ravel.teste.srm.strategy.ChangeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    private ExchangeTaxService exchangeTaxService;

    @Mock
    private ProductService productService;

    @Mock
    private CoinService coinService;

    @Mock
    private KingdomService kingdomService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ChangeStrategy defaultStrategy;

    @Mock
    private ChangeStrategy productStrategy;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Map<String, ChangeStrategy> strategyMap = new HashMap<>();
        strategyMap.put("DEFAULT_TAX", defaultStrategy);
        strategyMap.put("WOOD", productStrategy);


        transactionService = new TransactionService(
                exchangeTaxService,
                productService,
                coinService,
                kingdomService,
                transactionRepository,
                strategyMap
        );
    }

    @Test
    void createTransaction_shouldReturnDto_whenAllValid() {
        TransactionDTO dto = new TransactionDTO();
        dto.setProduct("Wood");
        dto.setOriginCoin("Gold");
        dto.setDestinationCoin("Silver");
        dto.setKingdom("North");

        Product product = new Product();
        product.setName("Wood");
        product.setBasePrice(new BigDecimal("100.00"));
        product.setId(1L);

        Coin origin = new Coin();
        origin.setName("Gold");

        Coin destiny = new Coin();
        destiny.setName("Silver");

        Kingdom kingdom = new Kingdom();
        kingdom.setKingdomName("North");

        ExchangeTax tax = new ExchangeTax();
        tax.setTax(new BigDecimal("0.1"));

        BigDecimal convertedAmount = new BigDecimal("110.00");

        when(productService.findByName("Wood")).thenReturn(product);
        when(coinService.getCoinByName("Gold")).thenReturn(origin);
        when(coinService.getCoinByName("Silver")).thenReturn(destiny);
        when(kingdomService.getKingdomByName("North")).thenReturn(kingdom);
        when(exchangeTaxService.getLatestTax(1L, "Gold", "Silver")).thenReturn(tax);
        when(productStrategy.change(product.getBasePrice(), tax)).thenReturn(convertedAmount);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionDateTime(LocalDateTime.now());
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction arg = invocation.getArgument(0);
            arg.setTransactionDateTime(savedTransaction.getTransactionDateTime());
            return arg;
        });

        TransactionResponseDTO responseDTO = transactionService.createTransaction(dto);

        assertNotNull(responseDTO);
        assertEquals("Wood", responseDTO.getProductName());
        assertEquals(product.getBasePrice(), responseDTO.getProductBasePrice());
        assertEquals(tax.getTax(), responseDTO.getTaxRate());
        assertEquals(convertedAmount, responseDTO.getConvertedAmount());
        assertEquals("North", responseDTO.getKingdomOfTransaction());
        assertEquals("Gold", responseDTO.getOriginCoin());
        assertEquals("Silver", responseDTO.getDestinyCoin());
        assertEquals(savedTransaction.getTransactionDateTime().toLocalDate(), responseDTO.getLocalDate());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void selectStrategy_shouldReturnDefaultWhenProductNotMapped() {
        Product product = new Product();
        product.setName("UnknownProduct");

        ChangeStrategy strategy = transactionService.selectStrategy(product);

        assertEquals(defaultStrategy, strategy);
    }

    @Test
    void toDTO_shouldMapTransactionToDTO() {
        Product product = new Product();
        product.setName("Wood");
        product.setBasePrice(new BigDecimal("100"));

        Coin origin = new Coin();
        origin.setName("Gold");

        Coin destiny = new Coin();
        destiny.setName("Silver");

        Kingdom kingdom = new Kingdom();
        kingdom.setKingdomName("North");

        ExchangeTax tax = new ExchangeTax();
        tax.setTax(new BigDecimal("0.1"));

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setOriginCoin(origin);
        transaction.setDestinyCoin(destiny);
        transaction.setKingdom(kingdom);
        transaction.setExchangeTax(tax);
        transaction.setAmountDestiny(new BigDecimal("110"));
        transaction.setTransactionDateTime(LocalDateTime.now());

        TransactionResponseDTO dto = transactionService.toDTO(transaction);

        assertEquals(product.getName(), dto.getProductName());
        assertEquals(product.getBasePrice(), dto.getProductBasePrice());
        assertEquals(tax.getTax(), dto.getTaxRate());
        assertEquals(transaction.getAmountDestiny(), dto.getConvertedAmount());
        assertEquals(kingdom.getKingdomName(), dto.getKingdomOfTransaction());
        assertEquals(origin.getName(), dto.getOriginCoin());
        assertEquals(destiny.getName(), dto.getDestinyCoin());
        assertEquals(transaction.getTransactionDateTime().toLocalDate(), dto.getLocalDate());
    }

    @Test
    void toDtoList_shouldMapPageTransactionToPageDto() {
        Product product = new Product();
        product.setName("Wood");
        product.setBasePrice(new BigDecimal("100"));

        Coin origin = new Coin();
        origin.setName("Gold");

        Coin destiny = new Coin();
        destiny.setName("Silver");

        Kingdom kingdom = new Kingdom();
        kingdom.setKingdomName("North");

        ExchangeTax tax = new ExchangeTax();
        tax.setTax(new BigDecimal("0.1"));

        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setOriginCoin(origin);
        transaction.setDestinyCoin(destiny);
        transaction.setKingdom(kingdom);
        transaction.setExchangeTax(tax);
        transaction.setAmountDestiny(new BigDecimal("110"));
        transaction.setTransactionDateTime(LocalDateTime.now());

        List<Transaction> transactions = List.of(transaction);

        Pageable pageable = PageRequest.of(0, 1);
        Page<Transaction> transactionPage = new PageImpl<>(transactions, pageable, 1);

        Page<TransactionResponseDTO> dtoPage = transactionService.toDtoList(transactionPage);

        assertEquals(1, dtoPage.getTotalElements());
        assertEquals(transaction.getProduct().getName(), dtoPage.getContent().getFirst().getProductName());
    }

    @Test
    void findAllPageabled_shouldCallRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(transactionRepository.findAll(pageable)).thenReturn(page);

        Page<Transaction> result = transactionService.findAllPageabled(0, 10);

        assertEquals(page, result);
        verify(transactionRepository, times(1)).findAll(pageable);
    }
}
