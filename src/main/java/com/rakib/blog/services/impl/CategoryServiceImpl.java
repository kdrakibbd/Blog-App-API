package com.rakib.blog.services.impl;

import com.rakib.blog.entities.Category;
import com.rakib.blog.exceptions.ResourceNotFoundException;
import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CategoryDto;
import com.rakib.blog.repository.CategoryRepo;
import com.rakib.blog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApiResponse createCategory(CategoryDto categoryDto) {
        Category category = this.modelMapper.map(categoryDto, Category.class);
        this.categoryRepo.save(category);
        return new ApiResponse("Category created successfully", true);
    }

    @Override
    public ApiResponse updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", categoryId));

        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        this.categoryRepo.save(category);
        return new ApiResponse("Category updated successfully", true);
    }

    @Override
    public ApiResponse deleteCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", categoryId));
        this.categoryRepo.delete(category);
        return new ApiResponse("Category deleted successfully", true);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "Category id", categoryId));

        return this.modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = this.categoryRepo.findAll();
        List<CategoryDto> categoryDtos = categories.stream().map((category -> this.modelMapper.map(category, CategoryDto.class))).collect(Collectors.toList());
        return categoryDtos;
    }
}
