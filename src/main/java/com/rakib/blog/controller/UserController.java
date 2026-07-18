package com.rakib.blog.controller;

import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.UserDto;
import com.rakib.blog.security.CurrentUser;
import com.rakib.blog.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "02. User Controller", description = "APIs for managing users")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Update an existing user", description = "Endpoint to update an existing user with the provided details")
    @PutMapping("/users")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@Valid @RequestBody UserDto userDto, @CurrentUser User user) {
        UserDto updated = this.userService.updateUser(userDto, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @Operation(summary = "Delete a user", description = "Endpoint to delete a user by their ID")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @Operation(summary = "Get all users", description = "Endpoint to retrieve a list of all users")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = this.userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @Operation(summary = "Get a user by ID", description = "Endpoint to retrieve a user by their ID")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Integer userId) {
        UserDto userDto = this.userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userDto));
    }

    @Operation(summary = "Upload user image", description = "Upload user image.")
    @PutMapping("/users/image")
    public ResponseEntity<ApiResponse<UserDto>> uploadUserImage(@CurrentUser User user, @RequestParam("image") MultipartFile image) throws IOException {
        UserDto updated = this.userService.uploadUserImage(user.getId(), image);
        return new ResponseEntity<>(ApiResponse.success("Image uploaded successfully", updated, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete user image", description = "Delete user image.")
    @DeleteMapping("/users/image")
    public ResponseEntity<ApiResponse<UserDto>> deleteUserImage(@CurrentUser User user) throws IOException {
        UserDto updated = this.userService.deleteUserImage(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Image deleted successfully", updated));
    }
}
