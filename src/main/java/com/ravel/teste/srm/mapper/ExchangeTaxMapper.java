package com.ravel.teste.srm.mapper;

import com.ravel.teste.srm.dto.ExchangeTaxDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.ExchangeTax;
import com.ravel.teste.srm.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ExchangeTaxMapper {


    @Mapping(source = "origin.name", target = "originCoinName")
    @Mapping(source = "destiny.name", target = "destinationCoinName")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "tax", target = "taxRate")
    ExchangeTaxDTO toDTO(ExchangeTax exchangeTax);

    @Mapping(source = "originCoinName", target = "origin", qualifiedByName = "mapCoinFromName")
    @Mapping(source = "destinationCoinName", target = "destiny", qualifiedByName = "mapCoinFromName")
    @Mapping(source = "productName", target = "product", qualifiedByName = "mapProductFromName")
    @Mapping(source = "taxRate", target = "tax")
    ExchangeTax toEntity(ExchangeTaxDTO exchangeTaxDTO);

    @Named("mapCoinFromName")
    default Coin mapCoinFromName(String name) {
        if (name == null) return null;
        Coin coin = new Coin();
        coin.setName(name);
        return coin;
    }


    @Named("mapProductFromName")
    default Product mapProductFromName(String name) {
        if (name == null) return null;
        Product product = new Product();
        product.setName(name);
        return product;
    }

}
