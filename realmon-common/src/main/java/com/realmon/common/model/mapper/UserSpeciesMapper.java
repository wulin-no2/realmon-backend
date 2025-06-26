package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.UserSpeciesDTO;
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
}
