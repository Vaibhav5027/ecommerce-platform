package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.http.ResponseEntity;
import  org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/public/categories")
    ResponseEntity<List<Category>> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }
    @PostMapping("/admin/categories")
    ResponseEntity<String> addCategory(@RequestBody Category category){
        String response = categoryService.addCategory(category);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PutMapping("/admin/categories/{categoryId}")
    ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        System.out.println(category.getCategoryName());
        Category updatedCategory = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }
    @DeleteMapping(("/admin/categories/{categoryId}"))
    ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try {
            String status = categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
