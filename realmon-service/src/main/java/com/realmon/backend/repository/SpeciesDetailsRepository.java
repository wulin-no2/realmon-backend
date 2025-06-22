package com.realmon.backend.repository;

import com.realmon.common.model.entity.SpeciesDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesDetailsRepository extends JpaRepository<SpeciesDetails, String> {
}

