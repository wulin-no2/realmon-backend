package com.realmon.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.backend.config.ChatGPTConfig;
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
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get species details from chatGPT
     * @param speciesName
     * @return
     */
    @Operation(summary = "Get species details from chatGPT")
    public Map<String, String> generateDetails(String speciesName) {
        String prompt = String.format(chatGPTConfig.getPromptTemplate(), speciesName);

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
            log.info("Content from chatGPT is {}", content);

            // Parse JSON output
            JsonNode json = objectMapper.readTree(content);
            Map<String, String> result = new HashMap<>();
            result.put("funFact", json.path("Fun Fact").asText());
            result.put("symbolism", json.path("Symbolism").asText());
            result.put("texture", json.path("Texture").asText());
            result.put("lifeCycle", json.path("Life Cycle").asText());
            result.put("protectionLevel", json.path("Protection Level").asText());
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Parsing GPT returned content failed: " + e.getMessage());
        }
    }
}
