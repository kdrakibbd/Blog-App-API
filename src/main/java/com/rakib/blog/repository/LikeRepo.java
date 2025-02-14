package com.rakib.blog.repository;

import com.rakib.blog.entities.Like;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Long countByPost(Post post);

}
