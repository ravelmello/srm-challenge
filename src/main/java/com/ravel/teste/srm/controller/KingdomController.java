package com.ravel.teste.srm.controller;


import com.ravel.teste.srm.dto.KingdomDTO;
import com.ravel.teste.srm.entity.Kingdom;
import com.ravel.teste.srm.service.KingdomService;
import com.ravel.teste.srm.utils.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Tag(name="Kingdom", description = "Endpoints to create a Kingdom")
@RestController
@RequestMapping("/v1/kingdom")
public class KingdomController {

    private final KingdomService kingdomService;

    public KingdomController(KingdomService kingdomService) {
        this.kingdomService = kingdomService;

    }

    @Operation(
            summary = "Create a new kingdom",
            description = "Creates a new kingdom with the given name and attributes. If the kingdom already exists, a conflict response is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kingdom created successfully"),
            @ApiResponse(responseCode = "409", description = "Kingdom already exists")
    })
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


    @Operation(
            summary = "Get all kingdoms",
            description = "Retrieves a list of all kingdoms currently registered in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of kingdoms retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<Kingdom>> getAllKingdoms(){
        List<Kingdom> kingdoms =  kingdomService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(kingdoms);
    }



}
