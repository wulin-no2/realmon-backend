package com.realmon.backend.service;

import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.common.model.dto.SpeciesDTO;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.mapper.SpeciesMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Species API", description = "Endpoints for managing species")
public class SpeciesService {

    private final SpeciesRepository repository;
    private final SpeciesMapper mapper;

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
}
