package com.realmon.backend.service;

import com.realmon.backend.repository.InatTokenRepository;
import com.realmon.common.model.entity.InatToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InatTokenService {

    private final InatTokenRepository tokenRepository;

    private String currentToken;

    @PostConstruct
    public void init() {
        tokenRepository.findAll().stream().findFirst().ifPresent(t -> {
            this.currentToken = t.getToken();
        });
        if (currentToken == null) {
            System.out.println("⚠️ No iNaturalist token found in DB. Please insert manually.");
        } else {
            System.out.println("✅ Loaded iNaturalist token from DB.");
        }
    }

    /** update token manually */
    public void updateToken(String newToken) {
        tokenRepository.deleteAll();
        InatToken tokenEntity = new InatToken();
        tokenEntity.setToken(newToken);
        tokenEntity.setUpdatedAt(LocalDateTime.now());
        tokenRepository.save(tokenEntity);
        this.currentToken = newToken;
        System.out.println("✅ iNaturalist token updated manually: " + newToken.substring(0, 10) + "...");
    }

    public String getToken() {
        return currentToken;
    }
}