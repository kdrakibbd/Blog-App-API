package com.rakib.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category title is required")
    @Size(min = 4, message = "Category title must be at least 4 characters")
    private String categoryTitle;

    @NotBlank(message = "Category description is required")
    @Size(min = 10, message = "Category description must be at least 10 characters")
    private String categoryDescription;
}
