package com.rakib.blog.services;

import com.rakib.blog.payloads.PostResponse;

import java.util.List;

public interface SavedPostService {
    String addPost(Integer userId, Integer postId);
    List<PostResponse> getSavedPostByUser(Integer userId);
}
