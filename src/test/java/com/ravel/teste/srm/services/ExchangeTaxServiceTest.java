package com.ravel.teste.srm.services;

import com.ravel.teste.srm.dto.ExchangeTaxDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.ExchangeTax;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.mapper.ExchangeTaxMapper;
import com.ravel.teste.srm.repository.ExchangeTaxRepository;
import com.ravel.teste.srm.service.CoinService;
import com.ravel.teste.srm.service.ExchangeTaxService;
import com.ravel.teste.srm.service.ProductService;
import com.ravel.teste.srm.utils.CoinNotExistsException;
import com.ravel.teste.srm.utils.ProductNotExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExchangeTaxServiceTest {

    @Mock
    private ExchangeTaxRepository exchangeTaxRepository;

    @Mock
    private CoinService coinService;

    @Mock
    private ProductService productService;

    @Mock
    private ExchangeTaxMapper exchangeTaxMapper;

    @InjectMocks
    private ExchangeTaxService exchangeTaxService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveNewExchangeTax_shouldSaveWhenValid() {
        ExchangeTaxDTO dto = new ExchangeTaxDTO();
        dto.setOriginCoinName("Real Gold");
        dto.setDestinationCoinName("Tibar");
        dto.setProductName("Wood");
        dto.setTaxRate(new BigDecimal("0.15"));

        Coin origin = new Coin();
        origin.setName("Real Gold");

        Coin destination = new Coin();
        destination.setName("Tibar");

        Product product = new Product();
        product.setName("Wood");

        when(coinService.getCoinByName("Real Gold")).thenReturn(origin);
        when(coinService.getCoinByName("Tibar")).thenReturn(destination);
        when(productService.findByName("Wood")).thenReturn(product);

        exchangeTaxService.saveNewExchangeTax(dto);

        verify(exchangeTaxRepository, times(1)).save(any(ExchangeTax.class));
    }

    @Test
    void saveNewExchangeTax_shouldThrowWhenOriginCoinNotExists() {
        ExchangeTaxDTO dto = new ExchangeTaxDTO();
        dto.setOriginCoinName("UnknownCoin");
        dto.setDestinationCoinName("Silver");
        dto.setProductName("Wood");

        when(coinService.getCoinByName("UnknownCoin")).thenReturn(null);

        CoinNotExistsException thrown = assertThrows(CoinNotExistsException.class, () -> exchangeTaxService.saveNewExchangeTax(dto));

        assertTrue(thrown.getMessage().contains("origin coin"));
        verify(exchangeTaxRepository, never()).save(any());
    }

    @Test
    void saveNewExchangeTax_shouldThrowWhenDestinationCoinNotExists() {
        ExchangeTaxDTO dto = new ExchangeTaxDTO();
        dto.setOriginCoinName("Gold");
        dto.setDestinationCoinName("UnknownCoin");
        dto.setProductName("Wood");

        when(coinService.getCoinByName("Gold")).thenReturn(new Coin());
        when(coinService.getCoinByName("UnknownCoin")).thenReturn(null);

        CoinNotExistsException thrown = assertThrows(CoinNotExistsException.class, () -> exchangeTaxService.saveNewExchangeTax(dto));

        assertTrue(thrown.getMessage().contains("destination coin"));
        verify(exchangeTaxRepository, never()).save(any());
    }

    @Test
    void saveNewExchangeTax_shouldThrowWhenProductNotExists() {
        ExchangeTaxDTO dto = new ExchangeTaxDTO();
        dto.setOriginCoinName("Gold");
        dto.setDestinationCoinName("Silver");
        dto.setProductName("UnknownProduct");

        when(coinService.getCoinByName("Gold")).thenReturn(new Coin());
        when(coinService.getCoinByName("Silver")).thenReturn(new Coin());
        when(productService.findByName("UnknownProduct")).thenReturn(null);

        ProductNotExistsException thrown = assertThrows(ProductNotExistsException.class, () -> exchangeTaxService.saveNewExchangeTax(dto));

        assertTrue(thrown.getMessage().contains("product name"));
        verify(exchangeTaxRepository, never()).save(any());
    }

    @Test
    void getTaxesList_shouldReturnDTOList() {
        ExchangeTax entity = new ExchangeTax();
        ExchangeTaxDTO dto = new ExchangeTaxDTO();

        List<ExchangeTax> entities = new ArrayList<>();
        entities.add(entity);

        when(exchangeTaxRepository.findAll()).thenReturn(entities);
        when(exchangeTaxMapper.toDTO(entity)).thenReturn(dto);

        List<ExchangeTaxDTO> result = exchangeTaxService.getTaxesList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());
    }

    @Test
    void getLatestTax_shouldCallRepository() {
        Long productId = 1L;
        String origin = "Real Gold";
        String destiny = "Tibar";

        ExchangeTax expected = new ExchangeTax();

        when(exchangeTaxRepository.searchLastTaxByProduct(productId, origin, destiny)).thenReturn(expected);

        ExchangeTax actual = exchangeTaxService.getLatestTax(productId, origin, destiny);

        assertEquals(expected, actual);
    }
}
