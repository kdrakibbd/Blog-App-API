package com.rakib.blog.services;

import com.rakib.blog.entities.Like;

public interface LikeService {
    String addLike(Integer userId, Integer postId);
    Long likeCountByPost(Integer postId);
}
