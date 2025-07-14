package com.realmon.common.model.entity;

public enum DailyQuestType {
    TODAYS_CREATURE,
    NEW_REALMON_CHALLENGE,
    BONUS_QUEST;
    public String getDisplayName() {
        return switch(this) {
            case TODAYS_CREATURE -> "Today's Creature";
            case NEW_REALMON_CHALLENGE -> "New Realmon Challenge";
            case BONUS_QUEST -> "Bonus Quest";
        };
    }
}
