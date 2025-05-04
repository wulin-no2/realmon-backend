package com.realmon.backend.repository;

import com.realmon.backend.model.Realmon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealmonRepository extends JpaRepository<Realmon, Long> {}