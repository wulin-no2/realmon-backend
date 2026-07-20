package com.realmon.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.realmon.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RealmonServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void cachedSpeciesDetailsRemainPublic() throws Exception {
        mockMvc.perform(get("/api/species/not-cached"))
                .andExpect(status().isNotFound());
    }

    @Test
    void speciesGenerationRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/species/not-cached/generate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registrationHashesPasswordAndDoesNotReturnIt() throws Exception {
        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"new-user","password":"secure-password"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new-user"))
                .andExpect(jsonPath("$.password").doesNotExist());

        String storedPassword = userRepository.findByUsername("new-user").orElseThrow().getPassword();
        assertFalse(storedPassword.equals("secure-password"));
        assertTrue(passwordEncoder.matches("secure-password", storedPassword));
    }

    @Test
    void loginUpgradesLegacyPlaintextPassword() throws Exception {
        userRepository.save(com.realmon.common.model.entity.User.builder()
                .username("legacy-user")
                .password("legacy-password")
                .source("local")
                .build());

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"legacy-user","password":"legacy-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());

        String upgradedPassword = userRepository.findByUsername("legacy-user").orElseThrow().getPassword();
        assertTrue(passwordEncoder.matches("legacy-password", upgradedPassword));
    }

}
