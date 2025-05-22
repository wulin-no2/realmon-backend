package com.realmon.backend.service;

import com.realmon.backend.repository.ObservationRepository;
import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.Observation;
import com.realmon.common.model.mapper.ObservationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Observation API", description = "Endpoints for managing observations")
public class ObservationService {

    private final ObservationRepository repository;
    private final ObservationMapper mapper;

    @Operation(summary = "Get all observations")
    public List<ObservationDTO> getAll() {
        List<Observation> list = repository.findAllWithSpeciesAndUser();
        log.info("Get all observations, {}", list);
        return mapper.toDTOs(list);
    }

    @Operation(summary = "Save observation info")
    public Observation save(Observation observation) {
        log.info("Save observation info, {}", observation);
        return repository.save(observation);
    }
}
