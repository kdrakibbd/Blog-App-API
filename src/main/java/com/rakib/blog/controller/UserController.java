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

import java.util.List;

@Tag(name = "02. User Controller", description = "APIs for managing users")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Update an existing user", description = "Endpoint to update an existing user with the provided details")
    @PutMapping("/users")
    public ResponseEntity<ApiResponse> updateUser(@Valid @RequestBody UserDto userDto, @CurrentUser User user) {
        ApiResponse response = this.userService.updateUser(userDto, user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a user", description = "Endpoint to delete a user by their ID")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {
        ApiResponse response = this.userService.deleteUser(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all users", description = "Endpoint to retrieve a list of all users")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @Operation(summary = "Get a user by ID", description = "Endpoint to retrieve a user by their ID")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }
}
