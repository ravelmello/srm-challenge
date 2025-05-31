package com.ravel.teste.srm.controller;


import com.ravel.teste.srm.dto.KingdomDTO;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.service.KingdomService;
import com.ravel.teste.srm.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/v1/kingdom")
public class KingdomController {

    private final KingdomService kingdomService;


    public KingdomController(KingdomService kingdomService) {
        this.kingdomService = kingdomService;

    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody KingdomDTO kingdomDTO) {
        try {
            kingdomService.saveKingdom(kingdomDTO);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Kingdom Already Exists", HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(kingdomDTO);
    }

    @GetMapping
    public ResponseEntity<List<Kingdom>> getAllKingdoms(){
        List<Kingdom> kingdoms =  kingdomService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(kingdoms);
    }



}
