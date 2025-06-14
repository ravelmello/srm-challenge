package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.CoinDTO;
import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.repository.CoinRepository;
import com.ravel.teste.srm.service.CoinService;
import com.ravel.teste.srm.utils.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="Coin", description = "Endpoints to define one coin of a Kingdom")
@RestController
@RequestMapping("/v1/coin")
public class CoinController {

    private final CoinService coinService;

    private static final Logger log = LoggerFactory.getLogger(CoinController.class);

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }


    @Operation(summary = "Make a creation of a coin", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "If the insertion is successfully"),
            @ApiResponse(responseCode = "404", description = "Kingdom not exists"),
            @ApiResponse(responseCode = "409", description = "Coin already exists in the kingdom")
    })
    @PostMapping
    public ResponseEntity<?> saveCoin(@RequestBody CoinDTO coinDTO) {
        try {
            coinService.saveKingdomCoin(coinDTO);
        } catch (IllegalStateException ex) {
            ErrorResponse error = new ErrorResponse("Kingdom doesn`t Exists", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Coin Already Exists", HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Recovery a definition of a Coin", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Body is empty, if not found a list of coins"),
    })
    @GetMapping
    public ResponseEntity<List<Coin>> getAllCoins() {
        List<Coin> coins = coinService.getAllCoins();
        return ResponseEntity.status(HttpStatus.OK).body(coins);
    }

}
