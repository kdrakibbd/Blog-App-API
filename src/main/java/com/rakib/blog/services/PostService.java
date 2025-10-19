package com.rakib.blog.services;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {
     ApiResponse createPost(PostDto postDto, Integer userId, Integer categoryId);
     ApiResponse updatePost(PostDto postDto, Integer categoryId, Integer postId, Integer userId);
     ApiResponse deletePost(Integer postId, Integer userId);
     PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     PostDto getPostById(Integer postId);
     List<PostDto> getPostByCategory(Integer categoryId);
     List<PostDto> getPostsByUser(Integer userId);
     List<PostDto> searchPost(String keyword);
}
