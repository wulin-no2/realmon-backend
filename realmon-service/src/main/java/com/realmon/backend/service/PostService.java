package com.realmon.backend.service;
import com.realmon.backend.repository.PostRepository;
import com.realmon.common.model.dto.PostDTO;
import com.realmon.common.model.entity.Observation;
import com.realmon.common.model.entity.Post;
import com.realmon.common.model.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    /**
     * Creates a Post entity from an Observation. called by userService.createPostFromObservation()
     * @param observation
     */
    public void createPostFromObservation(Observation observation) {
        Post post = Post.builder()
                .userId(observation.getUser().getId())
                .username(observation.getUser().getUsername())
                .avatarUrl(observation.getUser().getAvatarUrl())
                .speciesId(Long.valueOf(observation.getSpecies().getId()))
                .speciesName(observation.getSpecies().getName())
                .speciesIcon(observation.getSpecies().getIcon())
                .observationId(observation.getId())
                .imageUrl(observation.getImageUrl())
                .latitude(observation.getLatitude())
                .longitude(observation.getLongitude())
                .observedAt(observation.getObservedAt())
                .createdAt(observation.getObservedAt())
                .description("Spotted a " + observation.getSpecies().getName() + "!")
                .location(null) // can auto-generate location
                .build();

        postRepository.save(post);
    }

    public List<PostDTO> getRecentPosts() {
        return postRepository.findTop50ByOrderByCreatedAtDesc()
                .stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }
}