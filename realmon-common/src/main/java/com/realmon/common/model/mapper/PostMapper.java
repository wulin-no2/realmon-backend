package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.PostDTO;
import com.realmon.common.model.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO toDTO(Post entity);
    List<PostDTO> toDTOs(List<Post> entityList);

}

