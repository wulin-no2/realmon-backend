package com.realmon.backend.controller;

import com.realmon.backend.repository.NotificationRepository;
import com.realmon.backend.service.NotificationService;
import com.realmon.common.model.dto.NotificationDTO;
import com.realmon.common.model.entity.User;
import com.realmon.common.model.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> list() {
        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::toDTO)
                .toList();
    }

    @GetMapping("/test-push")
    public ResponseEntity<?> testPush(@AuthenticationPrincipal User user) {
        notificationService.sendTestNotification(user);
        return ResponseEntity.ok().build();
    }

}
