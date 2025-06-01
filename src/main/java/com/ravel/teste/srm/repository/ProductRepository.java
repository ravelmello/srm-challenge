package com.ravel.teste.srm.repository;

import com.ravel.teste.srm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
   Product findByName(String name);

    boolean existsProductByName(String productName);

    List<Product> findAll();
}
