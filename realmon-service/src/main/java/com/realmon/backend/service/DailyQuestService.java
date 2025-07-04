package com.realmon.backend.service;

import com.realmon.backend.repository.DailyQuestProgressRepository;
import com.realmon.backend.repository.UserRepository;
import com.realmon.common.model.dto.DailyQuestDTO;
import com.realmon.common.model.entity.DailyQuestProgress;
import com.realmon.common.model.entity.DailyQuestType;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyQuestService {

    private final DailyQuestProgressRepository dailyQuestProgressRepository;
    private final UserRepository userRepository;

    /**
     * initialise daily quests. initialise it once in /api/user/me
     */
    @Transactional
    public void initialiseDailyQuests(Long userId) {
        LocalDate today = LocalDate.now();

        for (DailyQuestType type : DailyQuestType.values()) {
            boolean exists = dailyQuestProgressRepository.findByUserIdAndDateAndQuestType(userId, today, type).isPresent();
            if (!exists) {
                DailyQuestProgress quest = new DailyQuestProgress();
                quest.setUserId(userId);
                quest.setDate(today);
                quest.setQuestType(type);
                quest.setProgress(0);
                quest.setCompleted(false);
                quest.setRewardCoins(getRewardForQuest(type));

                if (type.name().equals("BONUS_QUEST")) {
                    quest.setExtraInfo(generateRandomBonusQuest());
                }
                dailyQuestProgressRepository.save(quest);
            }
        }
    }

    /**
     * Update progress and give rewards
     */
    @Transactional
    public void updateQuestsAfterObservation(User user, boolean isNewSpecies, String speciesName) {
        LocalDate today = LocalDate.now();
        List<DailyQuestProgress> quests = dailyQuestProgressRepository.findAllByUserIdAndDate(user.getId(), today);

        for (DailyQuestProgress quest : quests) {
            if (quest.isCompleted()) continue;

            boolean completedNow = false;

            switch (quest.getQuestType()) {
                case TODAYS_CREATURE -> completedNow = true;

                case NEW_REALMON_CHALLENGE -> {
                    if (isNewSpecies) completedNow = true;
                }

                case BONUS_QUEST -> {
                    if (checkBonusQuestCondition(quest, speciesName)) {
                        completedNow = true;
                    }
                }
            }

            if (completedNow) {
                quest.setCompleted(true);
                quest.setProgress(1);
                // give rewards
                user.setCoins(user.getCoins() + getRewardForQuest(quest.getQuestType()));
                userRepository.save(user);
            }

            dailyQuestProgressRepository.save(quest);
            log.info("✅ Updated quest {}: completed={}, progress={}",
                    quest.getQuestType(), quest.isCompleted(), quest.getProgress());


        }
    }


    /**
     * generate Bonus Quest randomly
     */
    private String generateRandomBonusQuest() {
        List<String> options = List.of(
                "Find a Realmon with blue in its name",
                "Find a Realmon with green in its name",
                "Find a Realmon during sunny weather"
        );
        return options.get(new Random().nextInt(options.size()));
    }

    /**
     * check if Bonus Quest completed
     */
    private boolean checkBonusQuestCondition(DailyQuestProgress quest, String speciesName) {
        // todo: finish the completed logic
        String info = quest.getExtraInfo();
        if (info == null) return false;

        // for example, info contains "blue"
        if (info.contains("blue")) {
            return speciesName.toLowerCase().contains("blue");
        }
        return false;
    }

    /**
     * get all the status of totay's daily quests
     */
    public List<DailyQuestDTO> getTodayQuests(Long userId) {
        LocalDate today = LocalDate.now();
        List<DailyQuestProgress> quests = dailyQuestProgressRepository.findAllByUserIdAndDate(userId, today);
        List<DailyQuestDTO> dtos = new ArrayList<>();

        for (DailyQuestProgress quest : quests) {
            dtos.add(DailyQuestDTO.builder()
                    .id(quest.getId())
                    .questType(quest.getQuestType())
                    .progress(quest.getProgress())
                    .completed(quest.isCompleted())
                    .rewardCoins(quest.getRewardCoins())
                    .description(quest.getExtraInfo())
                    .build());
            log.info("🎯 Returning quest {}: completed={}, progress={}",
                    quest.getQuestType(), quest.isCompleted(), quest.getProgress());
        }



        return dtos;
    }



    /**
     * different coins for different quests
     */
    private int getRewardForQuest(DailyQuestType questType) {
        switch (questType) {
            case TODAYS_CREATURE: return 5;
            case NEW_REALMON_CHALLENGE: return 10;
            case BONUS_QUEST: return 18;
            default: return 0;
        }
    }
}
