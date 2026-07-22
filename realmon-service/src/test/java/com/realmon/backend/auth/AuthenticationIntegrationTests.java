package com.realmon.backend.auth;

import com.realmon.backend.repository.UserRepository;
import com.realmon.backend.utils.JwtUtil;
import com.realmon.common.model.entity.User;
import com.realmon.common.model.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthenticationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void duplicateUsernameRegistrationReturnsConflict() throws Exception {
        userRepository.save(localUser("duplicate-user", "secure-password"));

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"duplicate-user","password":"another-password"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void registrationCannotAssignAdminRole() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username":"role-escalation-user",
                                  "password":"secure-password",
                                  "role":"ADMIN"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").doesNotExist());

        User registered = userRepository.findByUsername("role-escalation-user").orElseThrow();
        assertEquals(UserRole.USER, registered.getRole());
    }

    @Test
    void registrationRejectsInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ab","password":"short"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bcryptLoginReturnsUserIdentityAndToken() throws Exception {
        User user = userRepository.save(localUser("bcrypt-login-user", "secure-password"));

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"bcrypt-login-user","password":"secure-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.username").value("bcrypt-login-user"));
    }

    @Test
    void wrongPasswordReturnsUnauthorized() throws Exception {
        userRepository.save(localUser("wrong-password-user", "secure-password"));

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"wrong-password-user","password":"incorrect-password"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void malformedJwtReturnsUnauthorizedForProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/user/me")
                        .header(AUTHORIZATION, "Bearer not-a-valid-jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtForMissingUserReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/me")
                        .header(AUTHORIZATION, "Bearer " + jwtUtil.generateToken(Long.MAX_VALUE)))
                .andExpect(status().isUnauthorized());
    }

    private User localUser(String username, String password) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .source("local")
                .role(UserRole.USER)
                .build();
    }
}
