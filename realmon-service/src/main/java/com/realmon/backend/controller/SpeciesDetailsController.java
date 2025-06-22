package com.realmon.backend.controller;

import com.realmon.backend.service.SpeciesDetailsAIService;
import com.realmon.backend.service.SpeciesService;
import com.realmon.common.model.dto.SpeciesDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
@Tag(name = "Species Details API", description = "Endpoints for viewing and generating species detail information")
public class SpeciesDetailsController {

    private final SpeciesDetailsAIService speciesDetailsAIService;
    private final SpeciesService speciesService;

    @GetMapping("/{id}")
    @Operation(summary = "Get species details by ID", description = "Returns AI-enhanced details for a species by ID")
    public SpeciesDetailsDTO getSpeciesDetailsById(@PathVariable String id) {
        return speciesDetailsAIService.getOrGenerateDetailsBySpeciesId(id);
    }

    @GetMapping("/generate-details")
    @Operation(summary = "Generate species details by name", description = "Returns species ID and generates details if species name is known")
    public ResponseEntity<SpeciesDetailsDTO> generateDetails(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String idByName = speciesService.getIdByName(name);
        SpeciesDetailsDTO result = speciesDetailsAIService.getOrGenerateDetailsBySpeciesId(idByName);
        return ResponseEntity.ok(result);
    }
}