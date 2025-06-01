package com.ravel.teste.srm.controller;

import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.service.ProductService;
import com.ravel.teste.srm.utils.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

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

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAllProducts () {
        List<ProductDTO> products = service.findAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

}
