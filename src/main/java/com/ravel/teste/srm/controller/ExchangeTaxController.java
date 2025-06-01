package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.ExchangeTaxDTO;
import com.ravel.teste.srm.service.ExchangeTaxService;
import com.ravel.teste.srm.utils.CoinNotExistsException;
import com.ravel.teste.srm.utils.ProductNotExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Exchange Tax", description = "Endpoints to calculate a price of a product at between different coins, based in a tax previously defined")
@RestController
@RequestMapping("/v1/exchange")
public class ExchangeTaxController {
    private final ExchangeTaxService exchangeTaxService;


    public ExchangeTaxController(ExchangeTaxService exchangeTaxService) {
        this.exchangeTaxService = exchangeTaxService;
    }


    @Operation(
            summary = "Create a new exchange tax",
            description = "Registers a new exchange tax between two currencies for a specific product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exchange tax successfully created"),
            @ApiResponse(responseCode = "404", description = "Coin or Product does not exist")
    })
    @PostMapping
    public ResponseEntity<?> saveExchangeTax(@RequestBody ExchangeTaxDTO exchangeTaxDTO) {
        try {
            exchangeTaxService.saveNewExchangeTax(exchangeTaxDTO);
        } catch (CoinNotExistsException | ProductNotExistsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Get all exchange taxes",
            description = "Retrieves a list of all registered exchange taxes between products and currencies."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of exchange taxes retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ExchangeTaxDTO>> getAllTaxes() {
        List<ExchangeTaxDTO> taxes = exchangeTaxService.getTaxesList();
        return ResponseEntity.ok().body(taxes);
    }


}
