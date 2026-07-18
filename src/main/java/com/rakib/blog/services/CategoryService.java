package com.rakib.blog.services;

import com.rakib.blog.payloads.CategoryRequest;
import com.rakib.blog.payloads.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(CategoryRequest request, Integer categoryId);
    void deleteCategory(Integer categoryId);
    CategoryResponse getCategory(Integer categoryId);
    List<CategoryResponse> getCategories();
}
