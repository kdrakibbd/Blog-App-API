package com.rakib.blog.controller;


import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CommentDto;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "05. Comment Controller", description = "APIs for managing comments on posts")
@RestController
@RequestMapping("/api/v1")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation( summary = "Create a new comment on a post", description = "Endpoint to create a new comment associated with a specific post and user")
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<ApiResponse> createComment(
            @RequestBody CommentDto comment,
            @PathVariable Integer postId,
            @CurrentUser User user) {
        ApiResponse response = this.commentService.createComment(comment, postId, user.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a comment", description = "Endpoint to delete a comment by its ID")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId, @CurrentUser User user) {
        ApiResponse response = this.commentService.deleteComment(commentId, user.getId());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
