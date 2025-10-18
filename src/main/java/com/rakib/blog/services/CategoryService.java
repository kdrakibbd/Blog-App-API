package com.rakib.blog.services;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {
    ApiResponse createCategory(CategoryDto categoryDto);
    ApiResponse updateCategory(CategoryDto categoryDto, Integer categoryId);
    ApiResponse deleteCategory(Integer categoryId);
    CategoryDto getCategory(Integer categoryId);
    List<CategoryDto> getCategories();
}
