package com.realmon.common.model.dto;

import com.realmon.common.model.entity.DailyQuestType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyQuestDTO {
    private Long id;

    private DailyQuestType questType;

    private int progress;

    private boolean completed;

    private int rewardCoins;

    private String description; // extraInfo
}
