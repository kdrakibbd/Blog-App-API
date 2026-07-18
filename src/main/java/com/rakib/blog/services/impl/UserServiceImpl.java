package com.rakib.blog.services.impl;

import com.rakib.blog.config.AppConstants;
import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ImageSizeExceededException;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.mappers.UserMapper;
import com.rakib.blog.payloads.UserDto;
import com.rakib.blog.repository.UserRepo;
import com.rakib.blog.services.ImageService;
import com.rakib.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setAbout(userDto.getAbout());

        User updated = this.userRepo.save(user);
        return this.userMapper.toDto(updated);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return this.userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream()
                .map(this.userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.userRepo.delete(user);
    }

    @Override
    public UserDto uploadUserImage(Integer userId, MultipartFile image) throws IOException {
        if (image.getSize() > AppConstants.USER_MAX_IMAGE_SIZE) {
            throw new ImageSizeExceededException("Image size must not exceed " + (AppConstants.USER_MAX_IMAGE_SIZE) / (1024 * 1024) + " MB");
        }

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        String imageUrl = this.imageService.uploadImage(image, AppConstants.USER_FOLDER);

        if (user.getImageUrl() != null) {
            try {
                this.imageService.deleteImage(user.getImageUrl(), AppConstants.USER_FOLDER);
            } catch (Exception e) {
                System.out.println("Failed to remove old image: " + e.getMessage());
            }
        }

        user.setImageUrl(imageUrl);
        User saved = this.userRepo.save(user);
        return this.userMapper.toDto(saved);
    }

    @Override
    public UserDto deleteUserImage(Integer userId) throws IOException {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        this.imageService.deleteImage(user.getImageUrl(), AppConstants.USER_FOLDER);

        user.setImageUrl(null);
        User saved = this.userRepo.save(user);
        return this.userMapper.toDto(saved);
    }
}
