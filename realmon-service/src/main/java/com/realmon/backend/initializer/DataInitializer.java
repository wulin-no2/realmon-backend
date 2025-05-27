package com.realmon.backend.initializer;

import com.realmon.backend.repository.ObservationRepository;
import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.backend.repository.UserRepository;
import com.realmon.common.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final ObservationRepository observationRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0 && speciesRepository.count() == 0) {
            // Create user
            User user = userRepository.save(User.builder()
                    .username("Lina")
                    .password("secret")
                    .source("local")
                    .avatarUrl("https://example.com/lina.jpg")
                    .build());

            // Create species
            Species robin = speciesRepository.save(Species.builder()
                    .id("robin") // can be replaced by iNaturalist ID
                    .name("European Robin")
                    .scientificName("Erithacus rubecula")
                    .category(SpeciesCategory.BIRD)
                    .wikiUrl("https://en.wikipedia.org/wiki/European_robin")
                    .build());

            // Create observation
            observationRepository.save(Observation.builder()
                    .latitude(53.3)
                    .longitude(-9.0)
                    .observedAt(LocalDateTime.now().minusDays(1))
                    .imageUrl("https://upload.wikimedia.org/wikipedia/commons/5/5f/Erithacus_rubecula_with_cocked_head.jpg")
                    .source("user-upload")
                    .species(robin)
                    .user(user)
                    .build());

            log.info("Sample data initialized.");
        }
    }
}
