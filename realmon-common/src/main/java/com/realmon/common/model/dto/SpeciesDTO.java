package com.realmon.common.model.dto;

import com.realmon.common.model.entity.SpeciesCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeciesDTO {
    private String id;
    private String name;
    private String scientificName;
    private String wikiUrl;
    private String icon;
    private SpeciesCategory category;
}