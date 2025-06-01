package com.ravel.teste.srm.mapper;

import com.ravel.teste.srm.dto.ProductDTO;
import com.ravel.teste.srm.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "name", target = "productName")
    @Mapping(source = "basePrice", target = "price")
    ProductDTO toDTO(Product product);

    @Mapping(source = "productName", target = "name")
    @Mapping(source = "price", target = "basePrice")
    Product toEntity(ProductDTO productDTO);
}
