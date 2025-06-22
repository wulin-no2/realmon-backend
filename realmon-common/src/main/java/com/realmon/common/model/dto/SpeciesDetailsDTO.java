package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeciesDetailsDTO {
    private String id;
    private String name;
    private String scientificName;
    private String wikiUrl;
    private String icon;
    private String category;

    private String funFact;
    private String symbolism;
    private String texture;
    private String lifeCycle;
    private String protectionLevel;
}
