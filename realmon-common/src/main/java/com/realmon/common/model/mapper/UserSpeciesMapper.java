package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.UserSpeciesDTO;
import com.realmon.common.model.dto.UserSpeciesViewDTO;
import com.realmon.common.model.entity.UserSpecies;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSpeciesMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "species.id", target = "speciesId")
    @Mapping(source = "observation.id", target = "observationId")
    UserSpeciesDTO toDTO(UserSpecies entity);

    List<UserSpeciesDTO> toDTOs(List<UserSpecies> entities);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "species.id", target = "speciesId")
    @Mapping(source = "observation.id", target = "observationId")
    @Mapping(source = "species.name", target = "speciesName")
    @Mapping(expression = "java(entity.getSpecies().getCategory() != null ? entity.getSpecies().getCategory().name() : null)", target = "speciesCategory")
    @Mapping(expression = "java(entity.getSpecies().getIcon())", target = "speciesIcon")
    @Mapping(expression = "java(entity.getSpecies().getImageUrls())", target = "speciesImageUrls")
    UserSpeciesViewDTO toViewDTO(UserSpecies entity);

    List<UserSpeciesViewDTO> toViewDTOs(List<UserSpecies> entities);

}
