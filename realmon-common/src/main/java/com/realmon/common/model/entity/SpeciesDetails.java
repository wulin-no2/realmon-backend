package com.realmon.common.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "species_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeciesDetails {

    @Id
    private String speciesId;  // same as Species.id

    private String name;
    private String scientificName;
    private String wikiUrl;
    private String icon;

    @Enumerated(EnumType.STRING)
    private SpeciesCategory category;

    @Column(columnDefinition = "TEXT")
    private String funFact;
    @Column(columnDefinition = "TEXT")
    private String symbolism;
    @Column(columnDefinition = "TEXT")
    private String texture;
    @Column(columnDefinition = "TEXT")
    private String lifeCycle;
    @Column(columnDefinition = "TEXT")
    private String protectionLevel;
}
