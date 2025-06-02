package com.ravel.teste.srm.services;


import com.ravel.teste.srm.dto.CoinDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.service.CoinService;
import com.ravel.teste.srm.service.KingdomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CoinServiceTest {
    @Mock
    private CoinRepository coinRepository;

    @Mock
    private KingdomService kingdomService;

    @InjectMocks
    private CoinService coinService;

    @Captor
    private ArgumentCaptor<Coin> coinCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveKingdomCoin_Success() {
        CoinDTO dto = new CoinDTO("Tibar", "Erebor", "TR$");

        Kingdom kingdom = new Kingdom();
        kingdom.setId(1);
        kingdom.setKingdomName("Erebor");
        kingdom.setDescription("Kingdom of Dwarfs");

        when(kingdomService.getKingdomByName("Erebor")).thenReturn(kingdom);

        coinService.saveKingdomCoin(dto);

        verify(coinRepository).save(coinCaptor.capture());
        Coin savedCoin = coinCaptor.getValue();

        assertEquals("Tibar", savedCoin.getName());
        assertEquals("TR$", savedCoin.getSymbol());
        assertEquals(kingdom, savedCoin.getKingdom());
    }

    @Test
    void saveKingdomCoin_KingdomNotFound_ThrowsException() {
        CoinDTO dto = new CoinDTO("Tibar", "Dwarf", "TR$");

        when(kingdomService.getKingdomByName("Dwarf")).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> coinService.saveKingdomCoin(dto));
        verify(coinRepository, never()).save(any());
    }

    @Test
    void getAllCoins_ReturnsList() {
        Coin coin1 = new Coin();
        coin1.setName("Tibar");

        Coin coin2 = new Coin();
        coin2.setName("Real Gold");

        when(coinRepository.findAll()).thenReturn(Arrays.asList(coin1, coin2));

        List<Coin> coins = coinService.getAllCoins();

        assertEquals(2, coins.size());
        assertEquals("Tibar", coins.get(0).getName());
        assertEquals("Real Gold", coins.get(1).getName());
    }

    @Test
    void getCoinByName_ReturnsCoin() {
        Coin coin = new Coin();
        coin.setName("Tibar");

        when(coinRepository.findByName("Tibar")).thenReturn(coin);

        Coin result = coinService.getCoinByName("Tibar");

        assertNotNull(result);
        assertEquals("Tibar", result.getName());
    }

}
