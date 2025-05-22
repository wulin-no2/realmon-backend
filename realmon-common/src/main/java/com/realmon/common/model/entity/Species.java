package com.realmon.common.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Species {

    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    private String id;  // can use iNaturalist taxon_id or UUID

    @ToString.Include
    private String name;

    private String scientificName;
    private String wikiUrl;
    private String icon;

    @Enumerated(EnumType.STRING)
    private SpeciesCategory category;
}