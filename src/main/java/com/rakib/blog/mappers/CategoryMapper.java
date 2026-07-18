package com.rakib.blog.mappers;

import com.rakib.blog.entities.Category;
import com.rakib.blog.payloads.CategoryRequest;
import com.rakib.blog.payloads.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getCategoryId());
        response.setCategoryTitle(category.getCategoryTitle());
        response.setCategoryDescription(category.getCategoryDescription());
        return response;
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        Category category = new Category();
        category.setCategoryTitle(request.getCategoryTitle());
        category.setCategoryDescription(request.getCategoryDescription());
        return category;
    }
}
