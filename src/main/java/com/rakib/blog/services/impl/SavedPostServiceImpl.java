package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.SavedPost;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.SavedPostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.SavedPostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedPostServiceImpl implements SavedPostService {

    @Autowired
    private SavedPostRepo savedPostRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String addPost(Integer userId, Integer postId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "ID", postId));

        Optional<SavedPost> savedPost = this.savedPostRepo.findByUserAndPost(user, post);

        if (savedPost.isPresent()) {
            this.savedPostRepo.delete(savedPost.get());
            return "Unsaved Post";
        }

        SavedPost s = new SavedPost();
        s.setUser(user);
        s.setPost(post);
        SavedPost save = this.savedPostRepo.save(s);

        return "Saved Post";
    }

    @Override
    public List<PostDto> getSavedPostByUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        List<SavedPost> allByUser = this.savedPostRepo.findAllByUser(user);

        return allByUser.stream()
                .map(post -> this.modelMapper.map(post.getPost(), PostDto.class))
                .collect(Collectors.toList());


    }
}
