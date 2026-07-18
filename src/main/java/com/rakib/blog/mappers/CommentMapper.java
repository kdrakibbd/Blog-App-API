package com.rakib.blog.mappers;

import com.rakib.blog.entities.Comment;
import com.rakib.blog.payloads.CommentDto;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    public Comment toEntity(CommentDto dto) {
        if (dto == null) return null;
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        return comment;
    }
}
