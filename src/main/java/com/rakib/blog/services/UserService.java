package com.rakib.blog.services;

import com.rakib.blog.payloads.UpdateUserRequest;
import com.rakib.blog.payloads.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResponse updateUser(UpdateUserRequest request, Integer userId);
    UserResponse getUserById(Integer userId);
    List<UserResponse> getAllUsers();
    void deleteUser(Integer userId);
    UserResponse uploadUserImage(Integer userId, MultipartFile image) throws IOException;
    UserResponse deleteUserImage(Integer userId) throws IOException;
}
