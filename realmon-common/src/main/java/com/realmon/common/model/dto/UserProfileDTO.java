package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * For /api/user/me to get user info and quest info when users open the app
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private UserDTO user;
    private List<DailyQuestDTO> dailyQuests;
}
