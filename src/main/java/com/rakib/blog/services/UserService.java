package com.rakib.blog.services;

import com.rakib.blog.payloads.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
    UserDto uploadUserImage(Integer userId, MultipartFile image) throws IOException;
    UserDto deleteUserImage(Integer userId) throws IOException;
}
