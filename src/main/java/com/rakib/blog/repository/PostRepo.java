package com.rakib.blog.repository;

import com.rakib.blog.entities.Category;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    //Custom finder
    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

    // Search Post
    List<Post> findByTitleContaining(String title);
}
