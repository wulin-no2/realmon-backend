package com.realmon.common.model.dto;



import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectRequestDTO {
    private String speciesId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private Instant timestamp;
    private String source;
}
