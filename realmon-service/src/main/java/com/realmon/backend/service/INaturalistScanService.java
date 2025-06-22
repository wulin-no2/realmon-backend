package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.backend.config.InaturalistConfig;
import com.realmon.backend.utils.MultipartInputStreamFileResource;
import com.realmon.common.model.dto.ScanResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Scan API", description = "Endpoints for scan species with iNaturalist API")
public class INaturalistScanService {
    // TODO: Add rate limiting to prevent abuse (e.g., max 1 request every 5 seconds per user)
    // TODO: Compress image before upload to improve speed and reduce payload
    // TODO: Implement fallback logic using local species database if API fails
    // TODO: Return top 1–3 results and let users confirm the correct species


    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InaturalistConfig inaturalistConfig;


    private static final String INAT_CV_URL = "https://api.inaturalist.org/v1/computervision/score_image";

    /**
     * Identify species with AI
     * @param imageFile
     * @return
     * @throws IOException
     */
    @Operation(summary = "Identify species with AI")
    public List<ScanResultDTO> identifySpecies(MultipartFile imageFile) throws IOException {
        log.info("Calling iNaturalist with image: {}", imageFile.getOriginalFilename());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + inaturalistConfig.getToken()); // iNaturalist token


        // Prepare the multipart request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new MultipartInputStreamFileResource(
                imageFile.getInputStream(),
                imageFile.getOriginalFilename())
        );
        System.out.println("filename = " + imageFile.getOriginalFilename());
        System.out.println("size = " + imageFile.getSize());


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                INAT_CV_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        log.info("Response from iNaturalist: {}", response.getBody());


        List<ScanResultDTO> results = new ArrayList<>();

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode resultList = root.path("results");

            for (JsonNode result : resultList) {
                JsonNode taxon = result.path("taxon");
                JsonNode photo = taxon.path("default_photo");

                results.add(ScanResultDTO.builder()
                        .id(taxon.path("id").asText(null))
                        .name(taxon.path("preferred_common_name").asText(null))
                        .scientificName(taxon.path("name").asText(null))
                        .wikiUrl(taxon.path("wikipedia_url").asText(null))
                        .category(taxon.path("iconic_taxon_name").asText(null))
                        .score(result.path("vision_score").asDouble(0))  // or combined_score
                        .imageUrl(photo.path("large_url").asText(photo.path("medium_url").asText(null)))
                        .build());
            }
        }

        return results;
    }
}
