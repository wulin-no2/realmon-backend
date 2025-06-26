package com.realmon.backend.repository;


import com.realmon.common.model.entity.UserSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSpeciesRepository extends JpaRepository<UserSpecies, Long> {

    List<UserSpecies> findByUserId(Long userId);

    boolean existsByUserIdAndSpeciesId(Long userId, String speciesId);

    Optional<UserSpecies> findByUserIdAndSpeciesId(Long userId, String speciesId);

}
