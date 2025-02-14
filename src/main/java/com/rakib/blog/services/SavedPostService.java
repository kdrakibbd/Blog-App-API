package com.rakib.blog.services;

import com.rakib.blog.payloads.PostDto;

import java.util.List;

public interface SavedPostService {
    String addPost(Integer userId, Integer postId);
    List<PostDto> getSavedPostByUser(Integer userId);
}
