package com.rakib.blog.services;

import com.rakib.blog.payloads.CommentRequest;
import com.rakib.blog.payloads.CommentResponse;

public interface CommentService {
    CommentResponse createComment(CommentRequest request, Integer postId, Integer userId);
    void deleteComment(Integer commentId, Integer userId);
    CommentResponse updateComment(CommentRequest request, Integer commentId, Integer userId);
}
