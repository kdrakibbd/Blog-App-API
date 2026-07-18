package com.rakib.blog.controller;

import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "06. Like Controller", description = "APIs for managing likes on posts")
@RestController
@RequestMapping("/api/v1")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Operation(summary = "Add a like to a post by a user", description = "Endpoint to add a like to a specific post by a specific user")
    @PostMapping("/post/{postId}/likes")
    public ResponseEntity<ApiResponse<String>> addLike(@CurrentUser User user, @PathVariable Integer postId) {
        String message = this.likeService.addLike(user.getId(), postId);
        return new ResponseEntity<>(ApiResponse.success(message, null, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @Operation(summary = "Count likes on a post", description = "Endpoint to count the number of likes on a specific post")
    @GetMapping("/post/{postId}/likes")
    public ResponseEntity<ApiResponse<Long>> countLikeByPost(@PathVariable Integer postId) {
        Long count = this.likeService.likeCountByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("Like count retrieved successfully", count));
    }
}
