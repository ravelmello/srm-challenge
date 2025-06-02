package com.ravel.teste.srm.services;

import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import com.ravel.teste.srm.mapper.ProductMapper;
import com.ravel.teste.srm.repository.ProductRepository;
import com.ravel.teste.srm.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProduct_Success() throws Exception {
        ProductDTO dto = new ProductDTO("Wood", new BigDecimal("10.0"));

        // Produto não existe
        when(productRepository.existsProductByName("Wood")).thenReturn(false);

        productService.saveProduct(dto);

        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();

        assertEquals("Wood", savedProduct.getName());
        assertEquals(new BigDecimal("10.0"), savedProduct.getBasePrice());
    }

    @Test
    void saveProduct_ProductAlreadyExists_ThrowsException() {
        ProductDTO dto = new ProductDTO("Wood", new BigDecimal("100.0"));

        when(productRepository.existsProductByName("Wood")).thenReturn(true);

        assertThrows(Exception.class, () -> productService.saveProduct(dto));
        verify(productRepository, never()).save(any());
    }

    @Test
    void findAllProducts_ReturnsMappedDTOList() {
        Product p1 = new Product();
        p1.setName("Wood");
        p1.setBasePrice(new BigDecimal("50.0"));

        Product p2 = new Product();
        p2.setName("Orc Skin");
        p2.setBasePrice(new BigDecimal("70.0"));

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        ProductDTO dto1 = new ProductDTO("Wood", new BigDecimal("50.0"));
        ProductDTO dto2 = new ProductDTO("Orc Skin", new BigDecimal("70.0"));

        when(productMapper.toDTO(p1)).thenReturn(dto1);
        when(productMapper.toDTO(p2)).thenReturn(dto2);

        List<ProductDTO> result = productService.findAllProducts();

        assertEquals(2, result.size());
        assertEquals("Wood", result.get(0).getProductName());
        assertEquals("Orc Skin", result.get(1).getProductName());
    }

    @Test
    void findByName_ReturnsProduct() {
        Product p = new Product();
        p.setName("Hidromel");

        when(productRepository.findByName("Hidromel")).thenReturn(p);

        Product result = productService.findByName("Hidromel");

        assertNotNull(result);
        assertEquals("Hidromel", result.getName());
    }
}
