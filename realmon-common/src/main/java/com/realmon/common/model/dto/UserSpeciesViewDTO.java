package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSpeciesViewDTO {
    private Long userId;
    private String speciesId;
    private Long observationId;
    private LocalDateTime collectedAt;

    private String speciesName;
    private String speciesCategory;
    private String speciesIcon;
    private List<String> speciesImageUrls;

}

