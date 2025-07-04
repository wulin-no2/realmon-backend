package com.realmon.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostDTO {
    private Long id;

    private Long userId;
    private String username;
    private String avatarUrl;

    private Long speciesId;
    private String speciesName;
    private String speciesIcon;

    private Long observationId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;

    private LocalDateTime observedAt;
    private LocalDateTime createdAt;

    private String description;
    private String location;
}
