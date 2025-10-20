package com.rakib.blog.services;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.payloads.PostResponse;
import org.springframework.web.multipart.MultipartFile;


public interface PostService {
     ApiResponse createPost(PostDto postDto, Integer userId, Integer categoryId, MultipartFile image) throws Exception;
     ApiResponse updatePost(PostDto postDto, Integer categoryId, Integer postId, Integer userId, MultipartFile image) throws Exception;
     ApiResponse deletePost(Integer postId, Integer userId);
     PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     PostDto getPostById(Integer postId);
     PostResponse getPostByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     PostResponse searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
     PostResponse getMyPosts(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
