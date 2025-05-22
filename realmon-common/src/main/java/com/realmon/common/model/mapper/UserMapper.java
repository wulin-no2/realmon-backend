package com.realmon.common.model.mapper;


import com.realmon.common.model.dto.UserDTO;
import com.realmon.common.model.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User entity);
    List<UserDTO> toDTOs(List<User> entities);
}