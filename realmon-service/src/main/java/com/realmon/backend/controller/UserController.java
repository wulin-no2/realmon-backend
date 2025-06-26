package com.realmon.backend.controller;

import com.realmon.backend.service.UserService;
import com.realmon.backend.utils.JwtUtil;
import com.realmon.common.model.dto.*;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers();
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
        return ResponseEntity.ok(collected);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(@AuthenticationPrincipal User user) {
        if (user == null) return ResponseEntity.status(401).build();

        UserDTO dto = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .source(user.getSource())
                .externalId(user.getExternalId())
                .avatarUrl(user.getAvatarUrl())
                .build();

        return ResponseEntity.ok(dto);
    }




}
