package com.realmon.backend.controller;

import com.realmon.backend.service.INaturalistService;
import com.realmon.backend.service.ObservationService;
import com.realmon.common.model.dto.ObservationCreateDTO;
import com.realmon.common.model.dto.ObservationDTO;
import com.realmon.common.model.entity.Observation;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/observations")
@RequiredArgsConstructor
@Slf4j
public class ObservationController {

    private final ObservationService service;
    private final INaturalistService iNaturalistService;


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

    @PostMapping("/create")
    public ResponseEntity<ObservationDTO> createObservation(
            @RequestBody ObservationCreateDTO dto,
            @AuthenticationPrincipal User user
    ) {
        ObservationDTO created = service.createObservation(dto, user);
        return ResponseEntity.ok(created);
    }



    /**
     * get nearby observations from iNaturalist and local db
     * @param lat
     * @param lon
     * @param radiusKm
     * @return
     */
    @GetMapping("/nearby")
    public List<ObservationDTO> getNearby(@RequestParam("lat") double lat,
                                          @RequestParam("lon") double lon,
                                          @RequestParam(name = "radiusKm", defaultValue = "5.0") double radiusKm
    ) {
        log.info("ObservationController hit");

        List<ObservationDTO> userObservations = service.findNearby(lat, lon, radiusKm);
        List<ObservationDTO> inatObservations = iNaturalistService.getNearbyFromINat(lat, lon, (int)radiusKm, 10);

        List<ObservationDTO> combined = new ArrayList<>();
        combined.addAll(userObservations);
        combined.addAll(inatObservations);
        return combined;
    }

}
