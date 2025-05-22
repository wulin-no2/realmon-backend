package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.RealmonDTO;
import com.realmon.common.model.entity.Realmon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RealmonMapper {
    RealmonDTO toDTO(Realmon entity);
    List<RealmonDTO> toDTOs(List<Realmon> entityList);
}