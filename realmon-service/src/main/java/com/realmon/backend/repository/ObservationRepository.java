package com.realmon.backend.repository;

import com.realmon.common.model.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    @Query("SELECT o FROM Observation o " +
            "LEFT JOIN FETCH o.species " +
            "LEFT JOIN FETCH o.user")
    List<Observation> findAllWithSpeciesAndUser();
}
