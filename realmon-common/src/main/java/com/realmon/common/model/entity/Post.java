package com.realmon.common.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.*;
import java.time.LocalDateTime;

import jakarta.persistence.GenerationType;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;
    private String avatarUrl;

    private Long speciesId;
    private String speciesName;
    private String speciesIcon;

    private Long observationId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private LocalDateTime observedAt; // when created the observation

    private String description;
    private LocalDateTime createdAt; // when created the post
    private String location; // e.g., "Central Park, NY, USA"
}
