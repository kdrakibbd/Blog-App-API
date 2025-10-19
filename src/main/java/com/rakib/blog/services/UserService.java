package com.rakib.blog.services;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    ApiResponse updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    ApiResponse deleteUser(Integer userId);
    ApiResponse uploadUserImage(Integer userId, MultipartFile image) throws IOException;
    ApiResponse deleteUserImage(Integer userId) throws IOException;
}
