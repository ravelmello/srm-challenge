package com.ravel.teste.srm.mapper;

import com.ravel.teste.srm.dto.CoinDTO;
import com.ravel.teste.srm.entity.Coin;
import com.ravel.teste.srm.entity.Kingdom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CoinMapper {
    @Mapping(source = "name", target = "nameCoin")
    @Mapping(source = "kingdom.kingdomName", target = "kingdom")
    CoinDTO toDTO(Coin entity);

    @Mapping(source = "nameCoin", target = "name")
    @Mapping(source = "kingdom", target = "kingdom", qualifiedByName = "mapKingdomFromName")
    Coin toEntity(CoinDTO dto);

    @Named("mapKingdomFromName")
    default Kingdom mapKingdomFromName(String name) {
        if (name == null) return null;
        Kingdom kingdom = new Kingdom();
        kingdom.setKingdomName(name);
        return kingdom;
    }

}
