package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.common.model.dto.SpeciesDTO;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.entity.SpeciesCategory;
import com.realmon.common.model.mapper.SpeciesMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Species API", description = "Endpoints for managing species")
public class SpeciesService {

    private final SpeciesRepository repository;
    private final SpeciesMapper mapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "Get all species")
    public List<SpeciesDTO> getAll() {
        List<Species> list = repository.findAll();
        log.info("all species info: {}", list);
        return mapper.toDTOs(list);
    }

    @Operation(summary = "Save species info")
    public Species save(Species species) {
        log.info("Save species info, {}", species);
        return repository.save(species);
    }

    @Operation(summary = "Get species name by id")
    public String getNameById(String id) {
        return repository.findById(id)
                .map(Species::getName)
                .orElseThrow(() -> new IllegalArgumentException("Species not found for id: " + id));
    }

    @Operation(summary = "Get species id by name")
    public String getIdByName(String name) {
        return repository.findByName(name)
                .map(Species::getId)
                .orElseThrow(() -> new IllegalArgumentException("Species not found for name: " + name));

    }

    /**
     * get images when creating or updating species
     * @param taxonId
     * @return
     */
    public Species createOrUpdateSpeciesWithImages(String taxonId) {
        Species species = repository.findById(taxonId)
                .orElseGet(() -> {
                    log.warn("Species {} not found in DB. Saving minimal record.", taxonId);
                    Species fallback = Species.builder()
                            .id(taxonId)
                            .name("[Unknown Species]")
                            .build();
                    return repository.save(fallback);
                });

        // if no images, get them from iNaturalist
        if (species.getImageUrl1() == null) {
            List<String> images = fetchTaxonImages(taxonId);
            if (images.size() > 0) species.setImageUrl1(images.get(0));
            if (images.size() > 1) species.setImageUrl2(images.get(1));
            if (images.size() > 2) species.setImageUrl3(images.get(2));
            repository.save(species);
            log.info("Fetched iNaturalist images: {}", images);
        }
        return species;
    }

    /**
     * get 3 images with iNaturalist api
     * @param taxonId
     * @return
     */
    private List<String> fetchTaxonImages(String taxonId) {
        String url = "https://api.inaturalist.org/v1/taxa/" + taxonId;
        try {
            ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
            JsonNode root = objectMapper.readTree(res.getBody());
            JsonNode photos = root.path("results").get(0).path("taxon_photos");

            List<String> urls = new ArrayList<>();
            for (int i = 0; i < Math.min(3, photos.size()); i++) {
                String imageUrl = photos.get(i).path("photo").path("medium_url").asText();
                urls.add(imageUrl);
            }
            return urls;
        } catch (Exception e) {
            log.warn("Failed to fetch images from iNaturalist: {}", e.getMessage());
            return List.of();
        }
    }


}
