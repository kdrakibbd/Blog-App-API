package com.rakib.blog.mappers;

import com.rakib.blog.entities.Post;
import com.rakib.blog.payloads.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PostMapper {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PostDto toDto(Post post) {
        if (post == null) return null;
        PostDto dto = new PostDto();
        dto.setPostId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setCategory(categoryMapper.toDto(post.getCategory()));
        dto.setUser(userMapper.toDto(post.getUser()));
        if (post.getComments() != null) {
            dto.setComments(
                    post.getComments().stream()
                            .map(commentMapper::toDto)
                            .collect(Collectors.toSet())
            );
        }
        return dto;
    }

    public Post toEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        return post;
    }
}
