package com.realmon.backend.security;

import com.realmon.backend.config.RateLimitProperties;
import com.realmon.common.model.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class ApiRateLimitFilter extends OncePerRequestFilter {

    private static final long WINDOW_MILLIS = 60_000L;

    private final RateLimitProperties properties;
    private final ConcurrentHashMap<String, WindowCounter> windows = new ConcurrentHashMap<>();
    private final AtomicLong attempts = new AtomicLong();
    private final Clock clock = Clock.systemUTC();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        return !path.equals("/api/scan")
                && !path.matches("^/api/species/[^/]+/generate$")
                && !path.equals("/api/species/generate-details");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long now = clock.millis();
        String path = request.getRequestURI();
        boolean scanRequest = path.equals("/api/scan");
        int limit = scanRequest
                ? properties.getScanRequestsPerMinute()
                : properties.getGenerationRequestsPerMinute();
        String bucket = scanRequest ? "scan" : "generation";
        String key = bucket + ":" + requestIdentity(request);

        WindowCounter counter = windows.computeIfAbsent(key, ignored -> new WindowCounter(now));
        long retryAfterSeconds = counter.acquire(now, limit);
        cleanupExpiredWindows(now);

        if (retryAfterSeconds > 0) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"rate_limit_exceeded\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String requestIdentity(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return "user:" + user.getId();
        }
        return "ip:" + request.getRemoteAddr();
    }

    private void cleanupExpiredWindows(long now) {
        if (attempts.incrementAndGet() % 256 == 0) {
            windows.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        }
    }

    private static final class WindowCounter {
        private long windowStartedAt;
        private int count;

        private WindowCounter(long now) {
            this.windowStartedAt = now;
        }

        private synchronized long acquire(long now, int limit) {
            if (now - windowStartedAt >= WINDOW_MILLIS) {
                windowStartedAt = now;
                count = 0;
            }
            if (count >= Math.max(1, limit)) {
                return Math.max(1, (WINDOW_MILLIS - (now - windowStartedAt) + 999) / 1000);
            }
            count++;
            return 0;
        }

        private synchronized boolean isExpired(long now) {
            return now - windowStartedAt >= WINDOW_MILLIS;
        }
    }
}
