package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.TransactionDTO;
import com.ravel.teste.srm.dto.TransactionResponseDTO;
import com.ravel.teste.srm.entity.Transaction;
import com.ravel.teste.srm.service.TransactionService;
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


    @PostMapping
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionResponseDTO dto = service.createTransaction(transactionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/search-full")
    public ResponseEntity<Page<Transaction>> listFull (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="10") int size){
        return ResponseEntity.ok(service.findAllPageabled(page, size));
    }


    @GetMapping("/search-compact")
    public ResponseEntity<Page<TransactionResponseDTO>> listCompact (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue="10") int size){
        Page<Transaction> transactionPage = service.findAllPageabled(page, size);
        Page<TransactionResponseDTO> dtoPage = transactionService.toDtoList(transactionPage);
        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }




}
