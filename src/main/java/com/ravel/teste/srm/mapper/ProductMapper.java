package com.ravel.teste.srm.mapper;

import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}
