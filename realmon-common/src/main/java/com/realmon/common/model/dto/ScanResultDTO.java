package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanResultDTO {
    private String id;
    private String name;            // common name
    private String scientificName;  // e.g., Rubus ulmifolius
    private String category;        // e.g., Plant
    private String icon;
    private String wikiUrl;         // link to Wikipedia
    private double score;           // confidence
    private String imageUrl;        // for display in frontend
}
