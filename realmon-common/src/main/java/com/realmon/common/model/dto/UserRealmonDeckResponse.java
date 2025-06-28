package com.realmon.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRealmonDeckResponse {
    private int totalCollected;
    private List<String> badges;
    private List<UserSpeciesViewDTO> items;
}
