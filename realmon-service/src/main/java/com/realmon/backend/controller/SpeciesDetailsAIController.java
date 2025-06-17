package com.realmon.backend.controller;

import com.realmon.backend.service.SpeciesDetailsAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
public class SpeciesDetailsAIController {

    private final SpeciesDetailsAIService aiService;

    @PostMapping("/generate-details")
    public ResponseEntity<Map<String, String>> generateDetails(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing species name"));
        }

        Map<String, String> result = aiService.generateDetails(name);
        return ResponseEntity.ok(result);
    }
}
