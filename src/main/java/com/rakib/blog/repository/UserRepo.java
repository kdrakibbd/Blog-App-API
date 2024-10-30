package com.rakib.blog.repository;

import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

}
