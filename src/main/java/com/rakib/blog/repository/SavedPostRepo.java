package com.rakib.blog.repository;

import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.SavedPost;
import com.rakib.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedPostRepo extends JpaRepository<SavedPost, Integer> {
    Optional<SavedPost> findByUserAndPost(User user, Post post);

    List<SavedPost> findAllByUser(User user);
}
