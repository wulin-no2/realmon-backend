package com.realmon.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class DailyQuestProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private DailyQuestType questType;

    private int progress;

    private boolean completed;

    private int rewardCoins;

    /**
     * store additional info with json, such as:
     * {"color":"green"} or {"weather":"rainy"}
     */
    @Column(columnDefinition = "TEXT")
    private String extraInfo; // e.g., "Find a blue Realmon";
}
