package com.rakib.blog.services;

import com.rakib.blog.payloads.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);
    void deleteComment(Integer commentId, Integer userId);
    CommentDto updateComment(CommentDto commentDto, Integer commentId, Integer userId);
}
