package com.realmon.backend.controller;

import com.realmon.backend.service.DailyQuestService;
import com.realmon.common.model.dto.DailyQuestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/daily-quest")
@RequiredArgsConstructor
@Tag(name = "Daily Quest api", description = "get daily quests status or update them")
public class DailyQuestController {

    private final DailyQuestService dailyQuestService;

    // initialise manually
    @PostMapping("/initialise")
    public void initialise(@RequestParam Long userId) {
        dailyQuestService.initialiseDailyQuests(userId);
    }

    // get all the quests for today
    @Operation(summary = "get today's daily quests")
    @GetMapping("/status")
    public List<DailyQuestDTO> getTodayStatus(@RequestParam Long userId) {
        return dailyQuestService.getTodayQuests(userId);
    }
}

