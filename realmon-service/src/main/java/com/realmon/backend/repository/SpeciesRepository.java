package com.realmon.backend.repository;

import com.realmon.common.model.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpeciesRepository extends JpaRepository<Species, String> {
    Optional<Species> findByName(String name);
    List<Species> findByNameContainingIgnoreCase(String keyword);


}