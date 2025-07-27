package com.realmon.backend.controller;


import com.realmon.backend.service.InatTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InatTokenService inatTokenService;

    @PostMapping("/refresh-token")
    public String refresh(@RequestBody String token) {
        inatTokenService.updateToken(token.trim());
        return "Refreshed token: " + token;
    }
}
