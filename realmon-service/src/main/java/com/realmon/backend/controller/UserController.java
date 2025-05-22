package com.realmon.backend.controller;

import com.realmon.backend.service.UserService;
import com.realmon.common.model.dto.UserDTO;
import com.realmon.common.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAllUsers();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return service.save(user);
    }
}
