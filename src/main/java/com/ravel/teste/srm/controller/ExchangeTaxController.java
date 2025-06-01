package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.ExchangeTaxDTO;
import com.ravel.teste.srm.service.ExchangeTaxService;
import com.ravel.teste.srm.utils.CoinNotExistsException;
import com.ravel.teste.srm.utils.ProductNotExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/exchange")
public class ExchangeTaxController {
    private final ExchangeTaxService exchangeTaxService;


    public ExchangeTaxController(ExchangeTaxService exchangeTaxService) {
        this.exchangeTaxService = exchangeTaxService;
    }

    //get all taxes
    @PostMapping
    public ResponseEntity<?> saveExchangeTax(@RequestBody ExchangeTaxDTO exchangeTaxDTO) {
        try {
            exchangeTaxService.saveNewExchangeTax(exchangeTaxDTO);
        } catch (CoinNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        } catch (ProductNotExistsException pr) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(pr);
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<ExchangeTaxDTO>> getAllTaxes() {
        List<ExchangeTaxDTO> taxes = exchangeTaxService.getTaxesList();
        return ResponseEntity.ok().body(taxes);
    }


}
