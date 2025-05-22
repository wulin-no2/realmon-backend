package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealmonDTO {
    private Long id;
    private String name;
    private String icon;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private String wikiUrl;
    private String species;

}