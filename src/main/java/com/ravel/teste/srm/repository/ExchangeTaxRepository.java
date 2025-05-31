package com.ravel.teste.srm.repository;

import com.ravel.teste.srm.entity.ExchangeTax;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeTaxRepository extends JpaRepository<ExchangeTax, Long> {
    Optional<ExchangeTax> findByProductId(Long productId);
}
