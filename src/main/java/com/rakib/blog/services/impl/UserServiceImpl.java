package com.rakib.blog.services.impl;

import com.rakib.blog.entities.User;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.mappers.UserMapper;
import com.rakib.blog.payloads.UpdateUserRequest;
import com.rakib.blog.payloads.UserResponse;
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

import static com.rakib.blog.config.AppConstants.USER_FOLDER;
import static com.rakib.blog.config.AppConstants.USER_MAX_IMAGE_SIZE;

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
    public UserResponse updateUser(UpdateUserRequest request, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        this.userMapper.applyToEntity(request, user);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        }

        User updated = this.userRepo.save(user);
        return this.userMapper.toResponse(updated);
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return this.userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return this.userRepo.findAll().stream()
                .map(this.userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.userRepo.delete(user);
    }

    @Override
    public UserResponse uploadUserImage(Integer userId, MultipartFile image) throws IOException {
        if (image.getSize() > USER_MAX_IMAGE_SIZE) {
            throw new com.rakib.blog.exceptions.ImageSizeExceededException(
                    "Image size must not exceed " + (USER_MAX_IMAGE_SIZE / (1024 * 1024)) + " MB");
        }

        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        String imageUrl = this.imageService.uploadImage(image, USER_FOLDER);

        if (user.getImageUrl() != null) {
            try {
                this.imageService.deleteImage(user.getImageUrl(), USER_FOLDER);
            } catch (Exception e) {
                System.out.println("Failed to remove old image: " + e.getMessage());
            }
        }

        user.setImageUrl(imageUrl);
        User saved = this.userRepo.save(user);
        return this.userMapper.toResponse(saved);
    }

    @Override
    public UserResponse deleteUserImage(Integer userId) throws IOException {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

        this.imageService.deleteImage(user.getImageUrl(), USER_FOLDER);

        user.setImageUrl(null);
        User saved = this.userRepo.save(user);
        return this.userMapper.toResponse(saved);
    }
}
