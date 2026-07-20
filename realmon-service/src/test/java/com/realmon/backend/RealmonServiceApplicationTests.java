package com.realmon.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RealmonServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

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

}
