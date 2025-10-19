package com.rakib.blog.repository;

import com.rakib.blog.entities.Category;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Integer> {
    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findByCategory(Category category, Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
}
