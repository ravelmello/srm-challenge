package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.TransactionDTO;
import com.ravel.teste.srm.dto.TransactionResponseDTO;
import com.ravel.teste.srm.entity.Transaction;
import com.ravel.teste.srm.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService service;
    private final TransactionService transactionService;

    public TransactionController(TransactionService service, TransactionService transactionService) {
        this.service = service;
        this.transactionService = transactionService;
    }


    @Operation(
            summary = "Execute a transaction",
            description = "Creates a new transaction between two kingdoms, converting currency according to the product and conversion rules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error during transaction execution")
    })
    @PostMapping
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionResponseDTO dto = service.createTransaction(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "List all transactions (full details)",
            description = "Retrieves a paginated list of all transactions with complete entity information, including raw transaction data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully")
    })
    @GetMapping("/search-full")
    public ResponseEntity<Page<Transaction>> listFull (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(service.findAllPageabled(page, size));
    }


    @Operation(
            summary = "List all transactions (compact view)",
            description = "Retrieves a paginated list of all transactions using a summarized DTO format, ideal for frontend display."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compact list of transactions retrieved successfully")
    })
    @GetMapping("/search-compact")
    public ResponseEntity<Page<TransactionResponseDTO>> listCompact (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="10") int size){
        Page<Transaction> transactionPage = service.findAllPageabled(page, size);
        Page<TransactionResponseDTO> dtoPage = transactionService.toDtoList(transactionPage);
        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }





}
