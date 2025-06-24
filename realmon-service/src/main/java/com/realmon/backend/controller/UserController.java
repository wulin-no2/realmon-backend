package com.realmon.backend.controller;

import com.realmon.backend.service.UserService;
import com.realmon.backend.utils.JwtUtil;
import com.realmon.common.model.dto.LoginRequest;
import com.realmon.common.model.dto.LoginResponse;
import com.realmon.common.model.dto.UserDTO;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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


}
