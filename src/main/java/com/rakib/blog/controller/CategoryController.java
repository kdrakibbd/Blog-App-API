package com.rakib.blog.controller;

import com.rakib.blog.payloads.ApiResponse;
import com.rakib.blog.payloads.CategoryDto;
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
    CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Endpoint to create a new category with the provided details")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        ApiResponse response = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing category", description = "Endpoint to update an existing category with the provided details")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId) {
        ApiResponse response = this.categoryService.updateCategory(categoryDto,categoryId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a category", description = "Endpoint to delete a category by its ID")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId) {
        ApiResponse response = this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get a category by ID", description = "Endpoint to retrieve a category by its ID")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
        CategoryDto categoryDto = this.categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @Operation(summary = "Get all categories", description = "Endpoint to retrieve a list of all categories")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = this.categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
