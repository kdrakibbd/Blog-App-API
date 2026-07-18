package com.rakib.blog.controller;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CategoryRequest;
import com.rakib.blog.payloads.CategoryResponse;
import com.rakib.blog.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "03. Category Controller", description = "APIs for managing categories")
@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Endpoint to create a new category with the provided details")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = this.categoryService.createCategory(request);
        return new ResponseEntity<>(ApiResponse.success("Category created successfully", created, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing category", description = "Endpoint to update an existing category with the provided details")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@Valid @RequestBody CategoryRequest request, @PathVariable Integer categoryId) {
        CategoryResponse updated = this.categoryService.updateCategory(request, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updated));
    }

    @Operation(summary = "Delete a category", description = "Endpoint to delete a category by its ID")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }

    @Operation(summary = "Get a category by ID", description = "Endpoint to retrieve a category by its ID")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Integer categoryId) {
        CategoryResponse response = this.categoryService.getCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", response));
    }

    @Operation(summary = "Get all categories", description = "Endpoint to retrieve a list of all categories")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<CategoryResponse> responses = this.categoryService.getCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", responses));
    }
}
