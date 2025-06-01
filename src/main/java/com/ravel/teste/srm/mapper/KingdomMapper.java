package com.ravel.teste.srm.mapper;

import com.ravel.teste.srm.dto.KingdomDTO;
import com.ravel.teste.srm.entity.Kingdom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KingdomMapper {
    KingdomDTO toDto(Kingdom kingdom);
    Kingdom toEntity(KingdomDTO kingdomDTO);
}
