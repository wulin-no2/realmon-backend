package com.realmon.backend.service;

import com.realmon.backend.repository.UserRepository;
import com.realmon.common.model.dto.UserDTO;
import com.realmon.common.model.entity.User;
import com.realmon.common.model.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Operation(summary = "Get all users")
    public List<UserDTO> getAllUsers() {
        List<User> list = repository.findAll();
        log.info("get all users{} ",list);
        return mapper.toDTOs(list);
    }

    @Operation(summary = "Save user info")
    public User save(User user) {
        log.info("save user info, {}", user);
        return repository.save(user);
    }

    @Operation(summary = "find user info by id")
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Operation(summary = "find user info by username and password")
    public User findByUsernameAndPassword(String username, String password) {
        return repository.findByUsernameAndPassword(username, password)
                .orElse(null); //
    }
}