package com.rakib.blog.controller;

import com.rakib.blog.entities.Post;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.services.SavedPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SavedPostController {

    @Autowired
    private SavedPostService savedPostService;

    @PostMapping("/user/{userId}/post/{postId}/save")
    public ResponseEntity<ApiResponse> addPost(@PathVariable Integer userId, @PathVariable Integer postId) {
        String message = this.savedPostService.addPost(userId, postId);
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/save")
    public ResponseEntity<List<PostDto>> getAllPostByUser(@PathVariable Integer userId) {
        List<PostDto> savedPostByUser = this.savedPostService.getSavedPostByUser(userId);
        return new ResponseEntity<>(savedPostByUser, HttpStatus.OK);
    }
}
