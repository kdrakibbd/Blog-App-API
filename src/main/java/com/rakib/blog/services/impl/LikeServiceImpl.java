package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Like;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.repository.LikeRepo;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Override
    public String addLike(Integer userId, Integer postId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "ID", postId));

        Optional<Like> likeByUserAndPost = this.likeRepo.findByUserAndPost(user, post);
        if (likeByUserAndPost.isPresent()) {
            this.likeRepo.delete(likeByUserAndPost.get());
            return "Unliked";
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        Like savedLike = this.likeRepo.save(like);

        return "Liked";
    }

    @Override
    public Long likeCountByPost(Integer postId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "ID", postId));
        return this.likeRepo.countByPost(post);
    }
}

