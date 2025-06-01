package com.ravel.teste.srm.service;


import com.ravel.teste.srm.dto.CoinDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.utils.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoinService.class);

    private final CoinRepository coinRepository;
    private final KingdomService kingdomService;

    public CoinService(CoinRepository coinRepository, KingdomService kingdomService) {
        this.coinRepository = coinRepository;
        this.kingdomService = kingdomService;
    }


    public void saveKingdomCoin(CoinDTO coinDTO) {
        LOGGER.info("Searching kingdom {} ", coinDTO.getKingdom());
        Kingdom kingdom = kingdomService.getKingdomByName(coinDTO.getKingdom());
        LOGGER.info("Kingdom found : {}", kingdom);

        if (kingdom == null) {
            throw new IllegalStateException();
        }

        Coin coin = new Coin();
        coin.setKingdom(kingdom);
        coin.setName(coinDTO.getNameCoin());
        coin.setSymbol(coinDTO.getSymbol());

        coinRepository.save(coin);
        LOGGER.info("Coin saved : {}", coin);
    }


    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }

    public Coin getCoinByName(String coinName) {
        return coinRepository.findByName(coinName);
    }
}
