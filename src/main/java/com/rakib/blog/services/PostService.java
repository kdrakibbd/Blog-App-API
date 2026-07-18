package com.rakib.blog.services;

import com.rakib.blog.payloads.PaginatedResponse;
import com.rakib.blog.payloads.PostDto;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId, MultipartFile image) throws Exception;
    PostDto updatePost(PostDto postDto, Integer categoryId, Integer postId, Integer userId, MultipartFile image) throws Exception;
    void deletePost(Integer postId, Integer userId);
    PaginatedResponse<PostDto> getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDto getPostById(Integer postId);
    PaginatedResponse<PostDto> getPostByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostDto> getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostDto> searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostDto> getMyPosts(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
