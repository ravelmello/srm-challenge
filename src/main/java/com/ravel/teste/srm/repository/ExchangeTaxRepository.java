package com.ravel.teste.srm.repository;

import com.ravel.teste.srm.entity.ExchangeTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExchangeTaxRepository extends JpaRepository<ExchangeTax, Long> {
    Optional<ExchangeTax> findByProductId(Long productId);

    @Query("""
    SELECT tax FROM ExchangeTax tax
        WHERE tax.product.id = :productID
            AND LOWER(tax.origin.name) = LOWER(:originName)
                AND LOWER(tax.destiny.name) = LOWER(:destinationName)
                    ORDER BY tax.taxDate DESC
    """)
    ExchangeTax searchLastTaxByProduct(@Param("productID") Long productID,
                                                 @Param("originName") String originCoin,
                                                 @Param("destinationName") String destinyCoin);
}
