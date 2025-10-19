package com.rakib.blog.services;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CommentDto;

public interface CommentService {
    ApiResponse createComment(CommentDto commentDto, Integer postId, Integer userId);
    ApiResponse deleteComment(Integer commentId, Integer userId);
}
