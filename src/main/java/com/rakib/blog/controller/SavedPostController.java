package com.rakib.blog.controller;

import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.SavedPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "07. Saved Post Controller", description = "APIs for managing saved posts by users")
@RestController
@RequestMapping("/api/v1")
public class SavedPostController {

    @Autowired
    private SavedPostService savedPostService;

    @Operation(summary = "Save a post for a user", description = "Endpoint to save a specific post for a specific user")
    @PostMapping("/posts/{postId}/save")
    public ResponseEntity<ApiResponse> addPostToSave(@CurrentUser User user, @PathVariable Integer postId) {
        String message = this.savedPostService.addPost(user.getId(), postId);
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all saved posts by a user", description = "Endpoint to retrieve all posts saved by a specific user")
    @GetMapping("/posts/save")
    public ResponseEntity<List<PostDto>> getAllPostByUser(@CurrentUser User user) {
        List<PostDto> savedPostByUser = this.savedPostService.getSavedPostByUser(user.getId());
        return new ResponseEntity<>(savedPostByUser, HttpStatus.OK);
    }
}
