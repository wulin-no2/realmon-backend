package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.backend.config.ChatGPTConfig;
import com.realmon.backend.repository.SpeciesDetailsRepository;
import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.common.model.dto.SpeciesDetailsDTO;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.entity.SpeciesDetails;
import com.realmon.common.model.mapper.SpeciesDetailsMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Species Details AI API from chatGPT", description = "Endpoints for getting species details like fun facts, etc")
public class SpeciesDetailsAIService {

    private final ChatGPTConfig chatGPTConfig;
    private final SpeciesRepository speciesRepository;
    private final SpeciesDetailsRepository speciesDetailsRepository;
    private final SpeciesDetailsMapper mapper;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * get species name by id
     * @param id
     * @return
     */
    public SpeciesDetailsDTO getOrGenerateDetailsBySpeciesId(String id) {
        return speciesDetailsRepository.findById(id)
                .map(mapper::toDTO)
                .orElseGet(() -> generateAndSave(id));
    }

    private SpeciesDetailsDTO generateAndSave(String speciesId) {

        Species species = speciesRepository.findById(speciesId)
                .orElseGet(() -> {
                    log.warn("Species {} not found in DB. Saving minimal record.", speciesId);
                    Species fallback = Species.builder()
                            .id(speciesId)
                            .name("[Unknown Species]")
                            .build();
                    return speciesRepository.save(fallback);
                });


        String prompt = chatGPTConfig.getPromptTemplate().formatted(species.getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(chatGPTConfig.getApikey());

        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", chatGPTConfig.getPromptStarter()),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = "https://api.openai.com/v1/chat/completions";

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String content = root.path("choices").get(0).path("message").path("content").asText();
            log.info("GPT raw content: {}", content);

            JsonNode json = objectMapper.readTree(content);

            SpeciesDetails entity = SpeciesDetails.builder()
                    .speciesId(species.getId())
                    .name(species.getName())
                    .icon(species.getIcon())
                    .scientificName(species.getScientificName())
                    .wikiUrl(species.getWikiUrl())
                    .category(species.getCategory())
                    .funFact(json.path("Fun Fact").asText())
                    .symbolism(json.path("Symbolism").asText())
                    .texture(json.path("Texture").asText())
                    .lifeCycle(json.path("Life Cycle").asText())
                    .protectionLevel(json.path("Protection Level").asText())
                    .build();

            speciesDetailsRepository.save(entity);
            log.info("species details are: {}",entity);
            return mapper.toDTO(entity);

        } catch (Exception e) {
            throw new RuntimeException("GPT JSON parse failed: " + e.getMessage());
        }
    }
}



///**
//     * Get species details from chatGPT
//     * @param speciesName
//     * @return
//     */
//    @Operation(summary = "Get species details from chatGPT")
//    public Map<String, String> generateDetails(String speciesName) {
//        String prompt = String.format(chatGPTConfig.getPromptTemplate(), speciesName);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(chatGPTConfig.getApikey());
//
//        Map<String, Object> body = Map.of(
//                "model", "gpt-3.5-turbo",
//                "messages", List.of(
//                        Map.of("role", "system", "content", chatGPTConfig.getPromptStarter()),
//                        Map.of("role", "user", "content", prompt)
//                ),
//                "temperature", 0.7
//        );
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        String url = "https://api.openai.com/v1/chat/completions";
//
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//
//        try {
//            JsonNode root = objectMapper.readTree(response.getBody());
//            String content = root.path("choices").get(0).path("message").path("content").asText();
//            log.info("Content from chatGPT is {}", content);
//
//            // Parse JSON output
//            JsonNode json = objectMapper.readTree(content);
//            Map<String, String> result = new HashMap<>();
//            result.put("funFact", json.path("Fun Fact").asText());
//            result.put("symbolism", json.path("Symbolism").asText());
//            result.put("texture", json.path("Texture").asText());
//            result.put("lifeCycle", json.path("Life Cycle").asText());
//            result.put("protectionLevel", json.path("Protection Level").asText());
//            return result;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Parsing GPT returned content failed: " + e.getMessage());
//        }
//    }

