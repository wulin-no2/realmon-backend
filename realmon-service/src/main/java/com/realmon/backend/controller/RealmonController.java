package com.realmon.backend.controller;

import com.realmon.backend.model.Realmon;
import com.realmon.backend.repository.RealmonRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/realmons")
public class RealmonController {

    private final RealmonRepository repository;

    public RealmonController(RealmonRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Realmon> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Realmon create(@RequestBody Realmon realMon) {
        return repository.save(realMon);
    }

    @GetMapping("/{id}")
    public Realmon getOne(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
}
