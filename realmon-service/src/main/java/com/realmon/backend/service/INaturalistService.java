//package com.realmon.backend.service;
//
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.realmon.common.model.dto.ObservationDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@Tag(name = "Inaturalist API", description = "Endpoints for getting data from Inaturalist")
//public class INaturalistService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    @Operation(summary = "Get nearby observations from iNaturalist")
//    public List<ObservationDTO> getNearbyFromINat(double lat, double lon) {
////        TODO: ADD CACHE WITH ConcurrentHashMap
////        TODO: import data to localDB every week instead of getting data from API
//        String url = String.format(
//                "https://api.inaturalist.org/v1/observations?lat=%f&lng=%f&radius=50&per_page=50&order=desc&order_by=observed_on",
//                lat, lon
//        ); // get 50 results from iNaturalist, order by observed time, radius 50
//
//        INatResponse response = restTemplate.getForObject(url, INatResponse.class);
//
//
//        return response.results.stream().map(obs -> {
//            ObservationDTO dto = new ObservationDTO();
//            dto.setLatitude(obs.geo.latitude);
//            dto.setLongitude(obs.geo.longitude);
//            dto.setObservedAt(LocalDateTime.parse(obs.observedAt.split("T")[0] + "T00:00:00")); // 只取日期
//            dto.setImageUrl(obs.getImageUrl());
//            dto.setSource("inaturalist");
//
//            dto.setSpeciesId(String.valueOf(obs.taxon.id));
//            dto.setSpeciesName(obs.taxon.name);
//            dto.setSpeciesIcon(obs.taxon.getIconUrl());
//            return dto;
//        }).collect(Collectors.toList());
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    private static class INatResponse {
//        public List<INatObservation> results;
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    private static class INatObservation {
//        @JsonProperty("observed_on")
//        public String observedAt;
//
//        @JsonProperty("geojson")
//        public Geo geo;
//
//        @JsonProperty("photos")
//        public List<Photo> photos;
//
//        @JsonProperty("taxon")
//        public Taxon taxon;
//
//        public String getImageUrl() {
//            return photos != null && !photos.isEmpty() ? photos.get(0).url : null;
//        }
//    }
//
//    private static class Geo {
//        public double latitude;
//        public double longitude;
//    }
//
//    private static class Photo {
//        @JsonProperty("url")
//        public String url;
//    }
//
//    private static class Taxon {
//        public long id;
//        public String name;
//
//        @JsonProperty("default_photo")
//        public Photo defaultPhoto;
//
//        public String getIconUrl() {
//            return defaultPhoto != null ? defaultPhoto.url : null;
//        }
//    }
//}
//
package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.SpeciesCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class INaturalistService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE_URL = "https://api.inaturalist.org/v1/observations";

    public List<ObservationDTO> getNearbyFromINat(double lat, double lon, int radiusKm, int limit) {
        //        TODO: ADD CACHE WITH ConcurrentHashMap
        //        TODO: import data to localDB every week instead of getting data from API
        String url = UriComponentsBuilder
                .fromHttpUrl(BASE_URL)
                .queryParam("lat", lat)
                .queryParam("lng", lon)
                .queryParam("radius", radiusKm)
                .queryParam("per_page", limit)
                .queryParam("order", "desc")
                .queryParam("order_by", "observed_on")
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode results = root.path("results");
                List<ObservationDTO> observations = new ArrayList<>();

                // get info from api
                for (JsonNode result : results) {
                    JsonNode taxon = result.path("taxon");
                    String speciesId = taxon.path("id").asText();
                    String speciesName = taxon.path("preferred_common_name").asText(null);
                    String scientificName = taxon.path("name").asText(null);
                    String iconUrl = taxon.path("default_photo").path("medium_url").asText(null);
                    String iconicName = taxon.path("iconic_taxon_name").asText(null);
                    String wikiUrl = taxon.path("wikipedia_url").asText(null);


                    SpeciesCategory category = mapToCategory(iconicName);
                    String speciesIcon = category.getDefaultIcon();

                    // get geo
                    JsonNode geo = result.path("geojson");
                    double latitude = geo.path("coordinates").get(1).asDouble();
                    double longitude = geo.path("coordinates").get(0).asDouble();

                    // get image
                    String imageUrl = null;

                    if (result.path("photos").isArray() && result.path("photos").size() > 0) {
                        JsonNode photo = result.path("photos").get(0);
                        String squareUrl = photo.path("url").asText(null);

                        if (squareUrl != null) {
                            imageUrl = squareUrl.replace("/square.jpg", "/large.jpg");
                        }
                    }
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = iconUrl;
                    }


                    // get user info
                    String username = result.path("user").path("login").asText("iNat-user");

                    String observedAtRaw = result.path("time_observed_at").asText(null);
                    LocalDateTime observedAt = observedAtRaw != null
                            ? OffsetDateTime.parse(observedAtRaw).toLocalDateTime()
                            : LocalDateTime.now();

                    // construct observation
                    observations.add(ObservationDTO.builder()
                            .latitude(latitude)
                            .longitude(longitude)
                            .observedAt(observedAt)
                            .imageUrl(imageUrl)
                            .source("inaturalist")
                            .speciesId(speciesId)
                            .speciesName(speciesName != null ? speciesName : scientificName)
                            .scientificName(scientificName)
                            .speciesIcon(speciesIcon)
                            .category(category.name())
                            .wikiUrl(wikiUrl)
                            .userId(null)
                            .username(username)
                            .build());
                }

                return observations;
            }
        } catch (Exception e) {
            log.error("Failed to fetch from iNaturalist API", e);
        }

        return List.of();
    }

    private SpeciesCategory mapToCategory(String iconicName) {
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
