package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Comment;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.exceptions.UnauthorizedException;
import com.rakib.blog.mappers.CommentMapper;
import com.rakib.blog.payloads.CommentRequest;
import com.rakib.blog.payloads.CommentResponse;
import com.rakib.blog.repository.CommentRepo;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(CommentRequest request, Integer postId, Integer userId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Comment comment = this.commentMapper.toEntity(request);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = this.commentRepo.save(comment);
        return this.commentMapper.toResponse(saved);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        this.commentRepo.delete(comment);
    }

    @Override
    public CommentResponse updateComment(CommentRequest request, Integer commentId, Integer userId) {
        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this comment");
        }
        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = this.commentRepo.save(comment);
        return this.commentMapper.toResponse(updated);
    }
}
