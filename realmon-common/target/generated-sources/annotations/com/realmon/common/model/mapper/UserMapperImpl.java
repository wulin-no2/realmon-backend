package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.UserDTO;
import com.realmon.common.model.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T00:47:45+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.11 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( entity.getId() );
        userDTO.username( entity.getUsername() );
        userDTO.source( entity.getSource() );
        userDTO.externalId( entity.getExternalId() );
        userDTO.avatarUrl( entity.getAvatarUrl() );

        return userDTO.build();
    }

    @Override
    public List<UserDTO> toDTOs(List<User> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( entities.size() );
        for ( User user : entities ) {
            list.add( toDTO( user ) );
        }

        return list;
    }
}
