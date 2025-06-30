package com.realmon.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//    private String icon;

    public String getIcon() {
        return category != null ? category.getDefaultIcon() : "❓";
    }

    @Enumerated(EnumType.STRING)
    private SpeciesCategory category;

    @Column(nullable = true)
    private String imageUrl1;
    @Column(nullable = true)
    private String imageUrl2;
    @Column(nullable = true)
    private String imageUrl3;

    public List<String> getImageUrls() {
        return Stream.of(imageUrl1, imageUrl2, imageUrl3)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}