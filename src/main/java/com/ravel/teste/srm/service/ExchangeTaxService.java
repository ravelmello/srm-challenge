package com.ravel.teste.srm.service;

import com.ravel.teste.srm.dto.ExchangeTaxDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.ExchangeTax;
import com.ravel.teste.srm.entity.Product;

import com.ravel.teste.srm.mapper.ExchangeTaxMapper;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.repository.ExchangeTaxRepository;
import com.ravel.teste.srm.utils.CoinNotExistsException;
import com.ravel.teste.srm.utils.ProductNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeTaxService {
    private final ExchangeTaxRepository exchangeTaxRepository;
    private final CoinService coinService;
    private final ProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeTaxService.class);
    private final ExchangeTaxMapper exchangeTaxMapper;

    public ExchangeTaxService(ExchangeTaxRepository exchangeTaxRepository,
                              CoinService coinService,
                              ProductService productService,
                              CoinRepository coinRepository, ExchangeTaxMapper exchangeTaxMapper) {
        this.exchangeTaxRepository = exchangeTaxRepository;
        this.coinService = coinService;
        this.productService = productService;
        this.exchangeTaxMapper = exchangeTaxMapper;
    }


    public void saveNewExchangeTax(ExchangeTaxDTO exchangeTaxDTO) {
        Coin origin = coinService.getCoinByName(exchangeTaxDTO.getOriginCoinName());
        if (origin == null) {
            throw new CoinNotExistsException("The origin coin -> " +exchangeTaxDTO.getOriginCoinName() + "does not exist");
        }

        Coin destination = coinService.getCoinByName(exchangeTaxDTO.getDestinationCoinName());
        if (destination == null) {
            throw new CoinNotExistsException("The destination coin -> " +exchangeTaxDTO.getDestinationCoinName() + "does not exist");
        }

        Product product = productService.findByName(exchangeTaxDTO.getProductName());
        if (product == null) {
            throw new ProductNotExistsException("The product name -> " + exchangeTaxDTO.getProductName() + "does not exist");
        }

        ExchangeTax exchangeTax = new ExchangeTax();
        exchangeTax.setOrigin(origin);
        exchangeTax.setDestiny(destination);
        exchangeTax.setProduct(product);
        exchangeTax.setTax(exchangeTaxDTO.getTaxRate());
        exchangeTax.setTaxDate(LocalDate.now());

        exchangeTaxRepository.save(exchangeTax);
        LOGGER.info("New exchange tax saved {} ", exchangeTax);

    }

    public List<ExchangeTaxDTO> getTaxesList() {
        return exchangeTaxRepository.findAll().stream().map(exchangeTaxMapper::toDTO).collect(Collectors.toList());
    }
}
