package com.rakib.blog.mappers;

import com.rakib.blog.entities.Comment;
import com.rakib.blog.payloads.CommentRequest;
import com.rakib.blog.payloads.CommentResponse;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) return null;
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }

    public Comment toEntity(CommentRequest request) {
        if (request == null) return null;
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        return comment;
    }
}
