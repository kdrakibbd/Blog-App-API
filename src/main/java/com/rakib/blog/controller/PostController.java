package com.rakib.blog.controller;

import com.rakib.blog.config.AppConstants;
import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.PostDto;
import com.rakib.blog.payloads.PostResponse;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "04. Post Controller", description = "APIs for managing blog posts")
@RestController
@RequestMapping("/api/v1")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "Create a new post", description = "Endpoint to create a new blog post associated with a user and category")
    @PostMapping(path = "/category/{categoryId}/posts", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> createPost(
            @Valid @RequestPart("post") PostDto postDto,
            @CurrentUser User user,
            @PathVariable Integer categoryId,
            @RequestPart(name = "image", required = false) MultipartFile image
    ) throws Exception {
        ApiResponse response = this.postService.createPost(postDto, user.getId(), categoryId, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a post", description = "Endpoint to update an existing blog post by its ID")
    @PutMapping(path = "/category/{categoryId}/posts/{postId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> updatePost(
            @Valid @RequestPart("post") PostDto postDto,
            @CurrentUser User user,
            @PathVariable Integer categoryId,
            @PathVariable Integer postId,
            @RequestPart(name = "image", required = false) MultipartFile image) throws Exception {
        ApiResponse response = postService.updatePost(postDto, categoryId, postId, user.getId(), image);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Delete a post", description = "Endpoint to delete a specific blog post by its ID")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId, @CurrentUser User user) {
        ApiResponse response = this.postService.deletePost(postId, user.getId());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Get all posts with pagination and sorting", description = "Endpoint to retrieve all blog posts with support for pagination and sorting")
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false ) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir)
    {
        PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @Operation(summary = "Get a post by ID", description = "Endpoint to retrieve a specific blog post by its ID")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto post = this.postService.getPostById(postId);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @Operation(summary = "Get all posts by category with pagination and sorting", description = "Endpoint to retrieve all blog posts associated with a specific category")
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<PostResponse> getPostsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse posts = this.postService.getPostByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @Operation(summary = "Get all posts by user with pagination and sorting", description = "Endpoint to retrieve all blog posts created by a specific user")
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<PostResponse> getPostsByUser(
            @PathVariable Integer userId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PostResponse response = this.postService.getPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Search posts by title with pagination with sorting", description = "Endpoint to search for blog posts by keywords in the title")
    @GetMapping("/posts/search/{keyword}")
    public ResponseEntity<PostResponse> searchPosts(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        PostResponse response = this.postService.searchPost(keyword, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get my posts with pagination and sorting", description = "Endpoint to retrieve all blog posts created by the currently authenticated user")
    @GetMapping("/posts/my")
    public ResponseEntity<PostResponse> getMyPosts(
            @CurrentUser User user,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse response = this.postService.getMyPosts(user.getId(), pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
