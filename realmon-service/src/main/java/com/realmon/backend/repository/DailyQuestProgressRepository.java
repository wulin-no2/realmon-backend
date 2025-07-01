package com.realmon.backend.repository;

import com.realmon.common.model.entity.DailyQuestProgress;
import com.realmon.common.model.entity.DailyQuestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyQuestProgressRepository extends JpaRepository<DailyQuestProgress, Long> {

    Optional<DailyQuestProgress> findByUserIdAndDateAndQuestType(Long userId, LocalDate date, DailyQuestType questType);

    List<DailyQuestProgress> findAllByUserIdAndDate(Long userId, LocalDate date);
}
