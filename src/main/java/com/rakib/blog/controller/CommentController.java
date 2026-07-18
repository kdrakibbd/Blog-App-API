package com.rakib.blog.controller;

import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CommentRequest;
import com.rakib.blog.payloads.CommentResponse;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(summary = "Create a new comment on a post", description = "Endpoint to create a new comment associated with a specific post and user")
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @Valid @RequestBody CommentRequest request,
            @PathVariable Integer postId,
            @CurrentUser User user) {
        CommentResponse created = this.commentService.createComment(request, postId, user.getId());
        return new ResponseEntity<>(ApiResponse.success("Comment created successfully", created, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a comment", description = "Endpoint to delete a comment by its ID")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Integer commentId, @CurrentUser User user) {
        this.commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully"));
    }

    @Operation(summary = "Update a comment", description = "Endpoint to update an existing comment by its ID")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @Valid @RequestBody CommentRequest request,
            @PathVariable Integer commentId,
            @CurrentUser User user) {
        CommentResponse updated = this.commentService.updateComment(request, commentId, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully", updated));
    }
}
