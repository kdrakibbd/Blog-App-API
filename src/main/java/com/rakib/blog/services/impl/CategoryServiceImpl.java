package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Category;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.mappers.CategoryMapper;
import com.rakib.blog.payloads.CategoryRequest;
import com.rakib.blog.payloads.CategoryResponse;
import com.rakib.blog.repository.CategoryRepo;
import com.rakib.blog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = this.categoryMapper.toEntity(request);
        Category saved = this.categoryRepo.save(category);
        return this.categoryMapper.toResponse(saved);
    }

    @Override
    public CategoryResponse updateCategory(CategoryRequest request, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

        category.setCategoryTitle(request.getCategoryTitle());
        category.setCategoryDescription(request.getCategoryDescription());
        Category updated = this.categoryRepo.save(category);
        return this.categoryMapper.toResponse(updated);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));
        this.categoryRepo.delete(category);
    }

    @Override
    public CategoryResponse getCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));
        return this.categoryMapper.toResponse(category);
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return this.categoryRepo.findAll().stream()
                .map(this.categoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}
