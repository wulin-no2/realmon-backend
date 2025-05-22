package com.realmon.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    private LocalDateTime observedAt;

    private Double latitude;
    private Double longitude;

    private String imageUrl;
    private String source;  // "user-upload", "inaturalist", etc.

    @ManyToOne(fetch = FetchType.LAZY)
    private Species species;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}