package com.realmon.backend.repository;

import com.realmon.common.model.entity.Realmon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealmonRepository extends JpaRepository<Realmon, Long> {}