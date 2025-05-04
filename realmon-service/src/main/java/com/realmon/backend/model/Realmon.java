package com.realmon.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Realmon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private LocalDateTime spottedAt;

    // Getters & Setters
}