package com.realmon.backend.security;

import com.realmon.backend.config.RateLimitProperties;
import com.realmon.common.model.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiRateLimitFilterTests {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void scanRequestsAreLimitedByClientAddress() throws Exception {
        RateLimitProperties properties = new RateLimitProperties();
        properties.setScanRequestsPerMinute(2);
        ApiRateLimitFilter filter = new ApiRateLimitFilter(properties);

        assertEquals(200, execute(filter, "/api/scan", "192.0.2.10").getStatus());
        assertEquals(200, execute(filter, "/api/scan", "192.0.2.10").getStatus());

        MockHttpServletResponse limited = execute(filter, "/api/scan", "192.0.2.10");
        assertEquals(429, limited.getStatus());
        assertEquals("60", limited.getHeader("Retry-After"));
    }

    @Test
    void generationRequestsAreLimitedByAuthenticatedUser() throws Exception {
        RateLimitProperties properties = new RateLimitProperties();
        properties.setGenerationRequestsPerMinute(1);
        ApiRateLimitFilter filter = new ApiRateLimitFilter(properties);
        User user = User.builder().id(42L).username("rate-limited-user").build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null));

        assertEquals(200, execute(filter, "/api/species/123/generate", "192.0.2.10").getStatus());
        assertEquals(429, execute(filter, "/api/species/456/generate", "192.0.2.11").getStatus());
    }

    private MockHttpServletResponse execute(ApiRateLimitFilter filter, String path, String address)
            throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
        request.setRemoteAddr(address);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        return response;
    }
}
