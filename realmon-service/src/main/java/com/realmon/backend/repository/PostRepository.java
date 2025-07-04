package com.realmon.backend.repository;

import com.realmon.common.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop50ByOrderByCreatedAtDesc();
}