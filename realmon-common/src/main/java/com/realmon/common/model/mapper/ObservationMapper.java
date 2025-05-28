package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.Observation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ObservationMapper {

    @Mapping(source = "species.id", target = "speciesId")
    @Mapping(source = "species.name", target = "speciesName")
    @Mapping(source = "species.icon", target = "speciesIcon")
    @Mapping(source = "species.category", target = "category")
    @Mapping(source = "species.wikiUrl", target = "wikiUrl")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    ObservationDTO toDTO(Observation entity);

    List<ObservationDTO> toDTOs(List<Observation> entities);
}