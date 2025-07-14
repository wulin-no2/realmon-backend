package com.realmon.common.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    @Column(length = 500)
    private String body;

    @Column(columnDefinition = "TEXT")
    private String dataJson; // for extra info like { "questId": 5, "coins": 30 }

    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // PENDING / SENT / FAILED

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;
}