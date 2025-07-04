package com.realmon.backend.controller;

import com.realmon.backend.service.PostService;
import com.realmon.common.model.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/recent")
    public ResponseEntity<List<PostDTO>> getRecentPosts() {
        List<PostDTO> posts = postService.getRecentPosts();
        return ResponseEntity.ok(posts);
    }
}