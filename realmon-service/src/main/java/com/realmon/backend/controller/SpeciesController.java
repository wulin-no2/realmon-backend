package com.realmon.backend.controller;

import com.realmon.backend.service.SpeciesService;
import com.realmon.common.model.dto.SpeciesDTO;
import com.realmon.common.model.entity.Species;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@RequiredArgsConstructor
public class SpeciesController {

    private final SpeciesService service;

    @GetMapping
    public List<SpeciesDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Species create(@RequestBody Species species) {
        return service.save(species);
    }
}
