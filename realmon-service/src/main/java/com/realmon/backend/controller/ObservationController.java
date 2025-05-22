package com.realmon.backend.controller;

import com.realmon.backend.service.ObservationService;
import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.Observation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
@RequiredArgsConstructor
public class ObservationController {

    private final ObservationService service;

    @GetMapping
    public List<ObservationDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Observation upload(@RequestBody Observation observation) {
        return service.save(observation);
    }
}
