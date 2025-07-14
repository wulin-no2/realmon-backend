package com.realmon.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDTO {
    private Long id;
    private String title;
    private String body;
    private String dataJson;
    private String status;
    private LocalDateTime createdAt;
}
