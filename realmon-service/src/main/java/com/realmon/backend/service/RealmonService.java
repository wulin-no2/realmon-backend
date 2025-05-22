package com.realmon.backend.service;

import com.realmon.backend.repository.RealmonRepository;
import com.realmon.common.model.dto.RealmonDTO;
import com.realmon.common.model.entity.Realmon;
import com.realmon.common.model.mapper.RealmonMapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RealmonService {

    private final RealmonRepository repository;
    private final RealmonMapper mapper;

    public List<RealmonDTO> getAllRealMons() {
        List<Realmon> list = repository.findAll();
        System.out.println("list in service:" + list);
//        return mapper.toDTOs(repository.findAll());
        return mapper.toDTOs(list);
    }

    public List<RealmonDTO> findNearby(double latitude, double longitude, double radiusKm) {
        List<Realmon> all = repository.findAll();
        return mapper.toDTOs(
                all.stream().filter(r -> isNearby(r.getLatitude(), r.getLongitude(), latitude, longitude, radiusKm))
                        .collect(Collectors.toList())
        );
    }
    private boolean isNearby(double lat1, double lon1, double lat2, double lon2, double radiusKm) {
        double R = 6371; // earth radius km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c <= radiusKm;
    }
}


