package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservationDTO {
    private Long id;
    private Double latitude;
    private Double longitude;
    private LocalDateTime observedAt;
    private String imageUrl;
    private String source;

    private String speciesId;
    private String speciesName;
    private String speciesIcon;

    private Long userId;
    private String username;
}