package com.realmon.backend.controller;

import com.realmon.backend.service.SpeciesDetailsAIService;
import com.realmon.backend.service.SpeciesService;
import com.realmon.common.model.dto.SpeciesDetailsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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
    @Operation(summary = "Get cached species details by ID", description = "Returns previously generated species details")
    public ResponseEntity<SpeciesDetailsDTO> getSpeciesDetailsById(@PathVariable String id) {
        return speciesDetailsAIService.getCachedDetailsBySpeciesId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/generate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Generate species details by ID", description = "Generates and stores species details when authenticated")
    public SpeciesDetailsDTO generateDetailsById(@PathVariable String id) {
        return speciesDetailsAIService.getOrGenerateDetailsBySpeciesId(id);
    }

    @PostMapping("/generate-details")
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
