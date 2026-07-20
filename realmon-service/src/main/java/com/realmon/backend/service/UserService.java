package com.realmon.backend.service;

import com.realmon.backend.repository.ObservationRepository;
import com.realmon.backend.repository.SpeciesRepository;
import com.realmon.backend.repository.UserRepository;
import com.realmon.backend.repository.UserSpeciesRepository;
import com.realmon.common.model.dto.*;
import com.realmon.common.model.entity.Observation;
import com.realmon.common.model.entity.Species;
import com.realmon.common.model.entity.User;
import com.realmon.common.model.entity.UserSpecies;
import com.realmon.common.model.mapper.UserMapper;
//import com.realmon.common.model.mapper.UserSpeciesMapper;
import com.realmon.common.model.mapper.UserSpeciesMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserService {

    private final UserRepository userRepository;
    private final SpeciesRepository speciesRepository;
    private final UserSpeciesRepository userSpeciesRepository;
    private final ObservationRepository observationRepository;
    private final UserMapper mapper;
    private final UserSpeciesMapper userSpeciesMapper;
    private final DailyQuestService dailyQuestService;
    private final PostService postService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;


    @Operation(summary = "Get all users")
    public List<UserDTO> getAllUsers() {
        List<User> list = userRepository.findAll();
        log.info("get all users{} ",list);
        return mapper.toDTOs(list);
    }

    @Operation(summary = "Save user info")
    public User save(User user) {
        log.info("save user info, {}", user);
        return userRepository.save(user);
    }

    public UserDTO register(RegisterRequest request) {
        String username = request.getUsername().trim();
        if (username.length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at least 3 characters");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .source("local")
                .role(com.realmon.common.model.entity.UserRole.USER)
                .build();
        return mapper.toDTO(userRepository.save(user));
    }

    @Operation(summary = "find user info by id")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Operation(summary = "find user info by username and password")
    public User authenticate(String username, String password) {
        Optional<User> result = userRepository.findByUsername(username.trim());
        if (result.isEmpty() || result.get().getPassword() == null) {
            return null;
        }

        User user = result.get();
        String storedPassword = user.getPassword();
        if (isBcryptHash(storedPassword)) {
            return passwordEncoder.matches(password, storedPassword) ? user : null;
        }

        if (!MessageDigest.isEqual(
                password.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8))) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private boolean isBcryptHash(String password) {
        return password.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }

    @Operation(summary = "user upload image and collects species")
    public UserSpeciesDTO collectSpecies(Long userId, CollectRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Species species = speciesRepository.findById(request.getSpeciesId())
                .orElseThrow(() -> new RuntimeException("Species not found"));

        // always create Observation
        Observation obs = null;
        if (request.getLatitude() != null && request.getLongitude() != null) {
            obs = Observation.builder()
                    .user(user)
                    .species(species)
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .observedAt(request.getTimestamp() != null ?
                            LocalDateTime.ofInstant(request.getTimestamp(), ZoneOffset.UTC) : LocalDateTime.now())
                    .imageUrl(request.getImageUrl())
                    .source(request.getSource())
                    .build();
            obs = observationRepository.save(obs);
        }

        // check if user_species exists and save it
        Optional<UserSpecies> existing = userSpeciesRepository
                .findByUserIdAndSpeciesId(userId, request.getSpeciesId());
        boolean isNewSpecies;

        UserSpecies userSpecies;
        if (existing.isPresent()) {
            userSpecies = existing.get(); // don't insert repeatedly
            isNewSpecies = false;
        } else {
            userSpecies = UserSpecies.builder()
                    .user(user)
                    .species(species)
                    .observation(obs)
                    .collectedAt(LocalDateTime.now())
                    .build();
            userSpecies = userSpeciesRepository.save(userSpecies);
            isNewSpecies = true;
        }

        // complete daily quest
        dailyQuestService.updateQuestsAfterObservation(findById(userId), isNewSpecies, species.getName());

        // create post
        postService.createPostFromObservation(obs);

        // send notification for new species
        if (isNewSpecies) {
            notificationService.createAndSend(
                    user,
                    "New Realmon Spotted!",
                    "You discovered a new species: " + species.getName(),
                    Map.of("speciesId", species.getId())
            );
        }

        return userSpeciesMapper.toDTO(userSpecies);
    }


    @Operation(summary = "get user collection deck")
    public UserRealmonDeckResponse getUserRealmonDeck(Long userId) {
        List<UserSpecies> entities = userSpeciesRepository.findByUserId(userId);
        List<UserSpeciesViewDTO> items = userSpeciesMapper.toViewDTOs(entities);

        int totalCollected = items.size();

        List<String> badges = entities.stream()
                .map(us -> us.getSpecies().getCategory())
                .filter(Objects::nonNull)
                .map(Enum::name)
                .distinct()
                .collect(Collectors.toList());

        return UserRealmonDeckResponse.builder()
                .totalCollected(totalCollected)
                .badges(badges)
                .items(items)
                .build();
    }

    public void updateExpoPushToken(User user, String token) {
        user.setExpoPushToken(token);
        userRepository.save(user);
    }


}
