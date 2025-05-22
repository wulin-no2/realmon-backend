package com.realmon.backend.repository;

import com.realmon.common.model.entity.Species;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepository extends JpaRepository<Species, String> {
}