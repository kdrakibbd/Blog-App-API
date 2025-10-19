package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Comment;
import com.rakib.blog.entities.Post;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CommentDto;
import com.rakib.blog.repository.CommentRepo;
import com.rakib.blog.repository.PostRepo;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse createComment(CommentDto commentDto, Integer postId, Integer userId) {
        Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId));
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);
        comment.setUser(user);
        this.commentRepo.save(comment);
        return new ApiResponse("Commented", true);
    }

    @Override
    public ApiResponse deleteComment(Integer commentId, Integer userId) {
        Comment comment = this.commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", commentId));
        if (!comment.getUser().getId().equals(userId)) {
            return new ApiResponse("You are not authorized to delete this comment", false);
        }
        this.commentRepo.delete(comment);
        return new ApiResponse("Comment deleted successfully", true);
    }
}
