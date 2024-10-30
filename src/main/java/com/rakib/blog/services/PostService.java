package com.rakib.blog.services;

import com.rakib.blog.entities.Post;
import com.rakib.blog.payloads.PostDto;

import java.util.List;

public interface PostService {
    //Create post
     PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

     //update post
     PostDto updatePost(PostDto postDto, Integer postId);

     //delete post
     void deletePost(Integer postId);

     // get all post
     List<PostDto> getAllPost();

     //get post by post id
     PostDto getPostById(Integer postId);

     //get all post by category
     List<PostDto> getPostByCategory(Integer categoryId);

     //get all post by user
     List<PostDto> getPostsByUser(Integer userId);

     // search post
     List<Post> searchPost(String keyword);
}
