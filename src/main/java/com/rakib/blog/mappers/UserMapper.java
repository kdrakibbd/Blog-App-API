package com.rakib.blog.mappers;

import com.rakib.blog.entities.User;
import com.rakib.blog.payloads.UpdateUserRequest;
import com.rakib.blog.payloads.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setAbout(user.getAbout());
        response.setImageUrl(user.getImageUrl());
        return response;
    }

    public void applyToEntity(UpdateUserRequest request, User user) {
        if (request == null || user == null) return;
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
        if (request.getAbout() != null) user.setAbout(request.getAbout());
    }
}
