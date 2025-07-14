package com.realmon.common.model.mapper;

import com.realmon.common.model.dto.NotificationDTO;
import com.realmon.common.model.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDTO toDTO(Notification entity);
}
