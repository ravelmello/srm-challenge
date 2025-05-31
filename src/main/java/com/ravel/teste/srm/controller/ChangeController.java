package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.ChangeRequestDTO;
import com.ravel.teste.srm.dto.ChangeResponseDTO;
import com.ravel.teste.srm.service.ChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/change")
public class ChangeController {

    private ChangeService service;

    public ChangeController(ChangeService service) {
        this.service = service;
    }

    public ChangeResponseDTO calculateProductValueOnDestiny(@RequestBody ChangeRequestDTO requestDTO) {
        return service.calculateProductValueOnDestiny(requestDTO);
    }

}
