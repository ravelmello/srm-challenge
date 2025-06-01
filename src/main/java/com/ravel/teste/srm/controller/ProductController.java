package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.service.ProductService;
import com.ravel.teste.srm.utils.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Product", description = "Endpoints to create a product and list the created products.")
@RestController
@RequestMapping("/v1/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create a new product",
            description = "Registers a new product in the system. A conflict error will be returned if the product already exists."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "409", description = "Product already exists")
    })
    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody ProductDTO productDTO) {
        try {
            service.saveProduct(productDTO);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Product Already Exists", HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(
            summary = "List all products",
            description = "Retrieves a list of all products registered in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAllProducts () {
        List<ProductDTO> products = service.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

}
