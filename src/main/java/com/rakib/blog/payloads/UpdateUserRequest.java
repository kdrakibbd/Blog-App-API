package com.rakib.blog.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(min = 4, message = "Username must be at least 4 characters")
    private String name;

    @Email(message = "Please provide a valid email address")
    private String email;

    @Size(min = 4, max = 20, message = "Password must be between 4 and 20 characters")
    private String password;

    private String about;
}
