package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Use it so that when collected in front end, we know updated progress and rewards immediately
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectResponseDTO {
    private UserSpeciesDTO collected;
    private List<DailyQuestDTO> dailyQuests;
    private int coins;
}

