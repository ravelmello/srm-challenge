package com.ravel.teste.srm.service;


import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(@RequestBody ProductDTO productDTO) throws Exception {
        if(verifyIfProductExists(productDTO.getProductName())){
            throw new Exception();
        }

        Product product = new Product();
        product.setName(productDTO.getProductName());
        product.setBasePrice(productDTO.getPrice());
        productRepository.save(product);

        LOGGER.info("Saved product {}", product.getName());
    }

    private boolean verifyIfProductExists(String productName) {
        return productRepository.existsProductByName(productName);
    }

    public List<Product>findAllProducts() {
        return productRepository.findAll();
    }
}
