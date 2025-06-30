package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.entity.SpeciesCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 1. turn iNaturalist taxon JSON into Species
 * 2. findOrCreate
 * 3. category mapping
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SpeciesSyncService {

    private final SpeciesRepository speciesRepository;

    public Species getOrCreateSpeciesFromTaxon(JsonNode taxon) {
        String id = taxon.path("id").asText(null);
        String name = taxon.path("preferred_common_name").asText(null);
        String scientificName = taxon.path("name").asText(null);
        String wikiUrl = taxon.path("wikipedia_url").asText(null);
        String categoryStr = taxon.path("iconic_taxon_name").asText(null);

        if (id == null) return null;

        SpeciesCategory category = mapToCategory(categoryStr);

        return speciesRepository.findById(id).orElseGet(() ->
                speciesRepository.save(Species.builder()
                        .id(id)
                        .name(name != null ? name : scientificName)
                        .scientificName(scientificName)
                        .wikiUrl(wikiUrl)
                        .category(category)
                        .build())
        );
    }

    public SpeciesCategory mapToCategory(String iconicName) {
        if (iconicName == null) return SpeciesCategory.OTHER;
        return switch (iconicName.toLowerCase()) {
            case "mammalia", "mammal" -> SpeciesCategory.MAMMAL;
            case "aves", "bird" -> SpeciesCategory.BIRD;
            case "reptilia", "reptile" -> SpeciesCategory.REPTILE;
            case "amphibia", "amphibian" -> SpeciesCategory.AMPHIBIAN;
            case "actinopterygii", "fish" -> SpeciesCategory.FISH;
            case "insecta", "insect" -> SpeciesCategory.INSECT;
            case "arachnida", "arachnid" -> SpeciesCategory.ARACHNID;
            case "crustacea" -> SpeciesCategory.CRUSTACEAN;
            case "mollusca", "mollusc" -> SpeciesCategory.MOLLUSC;
            case "fungi" -> SpeciesCategory.FUNGI;
            case "plantae", "plant" -> SpeciesCategory.PLANT;
            case "algae" -> SpeciesCategory.ALGAE;
            default -> SpeciesCategory.OTHER;
        };
    }
}

