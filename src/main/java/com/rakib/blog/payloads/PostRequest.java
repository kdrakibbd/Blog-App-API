package com.rakib.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    @NotBlank(message = "Post title is required")
    @Size(max = 100, message = "Post title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Post content is required")
    @Size(max = 10000, message = "Post content must not exceed 10000 characters")
    private String content;
}
