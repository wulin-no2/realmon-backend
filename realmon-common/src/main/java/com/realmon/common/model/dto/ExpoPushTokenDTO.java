package com.realmon.common.model.dto;

import lombok.Data;

// This class is used to transfer the Expo push token for notifications.
@Data
public class ExpoPushTokenDTO {
    private String expoPushToken;
}
