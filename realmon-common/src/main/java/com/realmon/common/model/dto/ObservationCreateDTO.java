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
public class ObservationCreateDTO {
    private String speciesId;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private String source;
    private LocalDateTime observedAt;
}

