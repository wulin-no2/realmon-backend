package com.realmon.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realmon.backend.repository.NotificationRepository;
import com.realmon.backend.repository.UserRepository;
import com.realmon.common.model.entity.Notification;
import com.realmon.common.model.entity.NotificationStatus;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    /**
     * Create and send notification
     */
    public void createAndSend(User user, String title, String body, Map<String, Object> data) {
        Notification notif = Notification.builder()
                .user(user)
                .title(title)
                .body(body)
                .dataJson(data != null ? serializeData(data) : null)
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notif = notificationRepository.save(notif);

        try {
            sendPushNotification(user, title, body, data);
            notif.setStatus(NotificationStatus.SENT);
            notif.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            notif.setStatus(NotificationStatus.FAILED);
            log.error("Failed to send push notification", e);
        }

        notificationRepository.save(notif);
    }

    private String serializeData(Map<String, Object> data) {
        try {
            return new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize notification data", e);
        }
    }

    /**
     * Call Expo API
     */
    private void sendPushNotification(User user, String title, String body, Map<String, Object> data) throws IOException, InterruptedException {
        if (user.getExpoPushToken() == null) {
            log.warn("User {} has no Expo Push Token", user.getId());
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        Map<String, Object> payload = new HashMap<>();
        payload.put("to", user.getExpoPushToken());
        payload.put("title", title);
        payload.put("body", body);
        if (data != null && !data.isEmpty()) {
            payload.put("data", data);
        }

        String json = new ObjectMapper().writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://exp.host/--/api/v2/push/send"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        log.info("Expo Push Response: {}", responseBody);
        // check invalid token
        if (responseBody.contains("\"status\":\"error\"")) {
            if (responseBody.contains("DeviceNotRegistered")) {
                // clear User token if it's invalid
                user.setExpoPushToken(null);
                userRepository.save(user);
                log.warn("Expo token for user {} marked INVALID (removed)", user.getId());
            }
        }
    }

    public void sendTestNotification(User user) {
        createAndSend(
                user,
                "🧪 Test Notification",
                "If you see this, your push works!",
                Map.of("test", true)
        );
    }

}
