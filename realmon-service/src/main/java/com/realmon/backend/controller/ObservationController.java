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


    /**
     * get all observations
     * @return
     */
    @GetMapping
    public List<ObservationDTO> getAll() {
        return service.getAll();
    }

    /**
     * save observation info
     * @param observation
     * @return
     */
    @PostMapping
    public Observation upload(@RequestBody Observation observation) {
        return service.save(observation);
    }

    @GetMapping("/nearby")
    public List<ObservationDTO> getNearBy(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "50.0") double radiumKm
    ){
        return service.findNearby(lat, lon, radiumKm);

    }
}
