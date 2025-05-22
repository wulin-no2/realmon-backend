package com.realmon.backend.controller;


import com.realmon.backend.repository.RealmonRepository;
import com.realmon.backend.service.RealmonService;
import com.realmon.common.model.dto.RealmonDTO;
import com.realmon.common.model.entity.Realmon;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/realmons")
public class RealmonController {

    private final RealmonRepository repository;
    private final RealmonService realmonService;

    public RealmonController(RealmonRepository repository, RealmonService realmonService) {
        this.repository = repository;
        this.realmonService = realmonService;
    }

    @GetMapping
    public List<RealmonDTO> getAll() {
        List<RealmonDTO> list = realmonService.getAllRealMons();
        System.out.println("list in controller:" + list );
        return list;
    }

    @PostMapping
    public Realmon create(@RequestBody Realmon realMon) {
        return repository.save(realMon);
    }

    @GetMapping("/{id}")
    public Realmon getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping("/nearby")
    public List<RealmonDTO> getNearbyRealmon(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "50.0") double radiusKm){
        return realmonService.findNearby(latitude, longitude, radiusKm);

    }
}

