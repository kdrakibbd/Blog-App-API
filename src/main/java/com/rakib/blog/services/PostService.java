package com.rakib.blog.services;

import com.rakib.blog.payloads.PaginatedResponse;
import com.rakib.blog.payloads.PostRequest;
import com.rakib.blog.payloads.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(PostRequest request, Integer userId, Integer categoryId, MultipartFile image) throws Exception;
    PostResponse updatePost(PostRequest request, Integer categoryId, Integer postId, Integer userId, MultipartFile image) throws Exception;
    void deletePost(Integer postId, Integer userId);
    PaginatedResponse<PostResponse> getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostResponse getPostById(Integer postId);
    PaginatedResponse<PostResponse> getPostByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostResponse> getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostResponse> searchPost(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PaginatedResponse<PostResponse> getMyPosts(Integer userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
