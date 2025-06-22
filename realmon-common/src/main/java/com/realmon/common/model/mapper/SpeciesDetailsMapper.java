package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.SpeciesDetailsDTO;
import com.realmon.common.model.entity.SpeciesDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SpeciesDetailsMapper {

    SpeciesDetailsMapper INSTANCE = Mappers.getMapper(SpeciesDetailsMapper.class);

    @Mapping(target = "icon", expression = "java(entity.getIcon())")
    SpeciesDetailsDTO toDTO(SpeciesDetails entity);
    SpeciesDetails toEntity(SpeciesDetailsDTO dto);
}