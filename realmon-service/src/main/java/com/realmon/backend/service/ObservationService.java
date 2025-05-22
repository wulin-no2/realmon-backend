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

    /**
     * Get all observation
     * @return
     */
    @Operation(summary = "Get all observations")
    public List<ObservationDTO> getAll() {
        List<Observation> list = repository.findAllWithSpeciesAndUser();
        log.info("Get all observations, {}", list);
        return mapper.toDTOs(list);
    }

    /**
     * save an observation
     * @param observation
     * @return
     */
    @Operation(summary = "Save observation info")
    public Observation save(Observation observation) {
        log.info("Save observation info, {}", observation);
        return repository.save(observation);
    }

    /**
     * find nearby observations
     * @param latitude
     * @param longitude
     * @param radiusKm
     * @return
     */
    @Operation(summary = "Get nearby observations")
    public List<ObservationDTO> findNearby(double latitude, double longitude, double radiusKm) {
        List<Observation> all = repository.findAllWithSpeciesAndUser();

        List<Observation> filtered = all.stream()
                .filter(obs -> isNearby(
                        obs.getLatitude(),
                        obs.getLongitude(),
                        latitude,
                        longitude,
                        radiusKm))
                .toList();

        return mapper.toDTOs(filtered);
    }

    private boolean isNearby(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= radiusKm;
    }

}
