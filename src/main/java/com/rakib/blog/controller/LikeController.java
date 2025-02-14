package com.rakib.blog.controller;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/user/{userId}/post/{postId}/likes")
    public ResponseEntity<ApiResponse> addLike(@PathVariable Integer userId, @PathVariable Integer postId) {
        String message = this.likeService.addLike(userId, postId);
        return new ResponseEntity<>(new ApiResponse(message, true), HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}/likes")
    public ResponseEntity<Map<String, Long>> countLikeByPost(@PathVariable Integer postId) {
        Long countByPost = this.likeService.likeCountByPost(postId);
        Map<String, Long> response = new HashMap<>();
        response.put("likeCount", countByPost);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
