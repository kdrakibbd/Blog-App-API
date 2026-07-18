package com.rakib.blog.mappers;

import com.rakib.blog.entities.Post;
import com.rakib.blog.payloads.PostRequest;
import com.rakib.blog.payloads.PostResponse;
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

    public PostResponse toResponse(Post post) {
        if (post == null) return null;
        PostResponse response = new PostResponse();
        response.setPostId(post.getPostId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setImageUrl(post.getImageUrl());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        response.setCategory(categoryMapper.toResponse(post.getCategory()));
        response.setUser(userMapper.toResponse(post.getUser()));
        if (post.getComments() != null) {
            response.setComments(
                    post.getComments().stream()
                            .map(commentMapper::toResponse)
                            .collect(Collectors.toSet())
            );
        }
        return response;
    }

    public Post toEntity(PostRequest request) {
        if (request == null) return null;
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return post;
    }
}
