package com.realmon.backend.controller;

import com.realmon.backend.service.DailyQuestService;
import com.realmon.backend.service.UserService;
import com.realmon.backend.utils.JwtUtil;
import com.realmon.common.model.dto.*;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final DailyQuestService dailyQuestService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers();
    }


    @GetMapping("/collected")
    public ResponseEntity<UserRealmonDeckResponse> getRealmonDeck(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getUserRealmonDeck(user.getId()));
    }


    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = service.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getId());
        return ResponseEntity.ok(new LoginResponse(token, user.getId(), user.getUsername()));
    }

    @PostMapping("/collect")
    public ResponseEntity<?> collectSpecies(
            @RequestBody CollectRequestDTO request,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getId();
        UserSpeciesDTO collected = service.collectSpecies(userId, request);

//        return ResponseEntity.ok(collected);
        // refresh user to avoid cache
        User updatedUser = service.findById(userId);

        // get updated daily quest
        List<DailyQuestDTO> todayQuests = dailyQuestService.getTodayQuests(userId);

        return ResponseEntity.ok(
                CollectResponseDTO.builder()
                        .collected(collected)
                        .dailyQuests(todayQuests)
                        .coins(updatedUser.getCoins())
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMe(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();
        // initialise daily quests
        dailyQuestService.initialiseDailyQuests(user.getId());
        List<DailyQuestDTO> todayQuests = dailyQuestService.getTodayQuests(user.getId());

        // return user info
        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .coins(user.getCoins())
                .username(user.getUsername())
                .source(user.getSource())
                .externalId(user.getExternalId())
                .avatarUrl(user.getAvatarUrl())
                .build();
        UserProfileDTO profile = UserProfileDTO.builder()
                .user(dto)
                .dailyQuests(todayQuests)
                .build();

        return ResponseEntity.ok(profile);
    }


    @PostMapping("/me/push-token")
    public ResponseEntity<?> saveExpoPushToken(
            @RequestBody ExpoPushTokenDTO dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("Saving ExpoPushToken for user {}: {}", user.getId(), dto.getExpoPushToken());

        user.setExpoPushToken(dto.getExpoPushToken());
        service.save(user);
        return ResponseEntity.ok().build();
    }





}
