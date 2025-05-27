package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.SpeciesDTO;
import com.realmon.common.model.entity.Species;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpeciesMapper {
    @Mapping(target = "icon", expression = "java(entity.getIcon())")
    SpeciesDTO toDTO(Species entity);
    List<SpeciesDTO> toDTOs(List<Species> entities);

    Species toEntity(SpeciesDTO dto);
}