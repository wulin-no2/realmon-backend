package com.realmon.backend.service;

import com.realmon.backend.repository.RealmonRepository;
import com.realmon.common.model.dto.RealmonDTO;
import com.realmon.common.model.entity.Realmon;
import com.realmon.common.model.mapper.RealmonMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Tag(name = "Realmon API", description = "Endpoints for managing realmons")
public class RealmonService {

    private final RealmonRepository repository;
    private final RealmonMapper mapper;

    @Operation(summary = "Get all realmons")
    public List<RealmonDTO> getAllRealMons() {
        List<Realmon> list = repository.findAll();
        log.info("get all realmons{} ",list);
        return mapper.toDTOs(list);
    }
    @Operation(summary = "Get nearby realmons")
    public List<RealmonDTO> findNearby(double latitude, double longitude, double radiusKm) {
        List<Realmon> all = repository.findAll();
        List<Realmon> filtered = all.stream()
                .filter(r -> isNearby(r.getLatitude(), r.getLongitude(), latitude, longitude, radiusKm))
                .collect(Collectors.toList());

        log.info("Found {} realmons near ({}, {}) within {} km", filtered.size(), latitude, longitude, radiusKm);
        return mapper.toDTOs(filtered);
    }
    private boolean isNearby(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        double R = 6371; // Earth's radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance <= radiusKm;
    }

}


