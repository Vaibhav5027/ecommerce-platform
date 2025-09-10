package com.ecommerce.project.controller;

import com.ecommerce.project.config.CategoryApiConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import  org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/public/categories")
    ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = CategoryApiConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = CategoryApiConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = CategoryApiConstants.ORDER_BY, required = false) String orderBy)
    {

        CategoryResponse allCategories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,orderBy);
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }
    @PostMapping("/admin/categories")
    ResponseEntity<CategoryDTO> addCategory( @Valid  @RequestBody Category category){
        CategoryDTO categoryDTO = categoryService.addCategory(category);
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }
    @PutMapping("/admin/categories/{categoryId}")
    ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody Category category, @PathVariable Long categoryId){
        System.out.println(category.getCategoryName());
        CategoryDTO categoryDTO = categoryService.updateCategory(category, categoryId);
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }
    @DeleteMapping(("/admin/categories/{categoryId}"))
    ResponseEntity<CategoryDTO> deleteCategory( @PathVariable Long categoryId){
            CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);

    }
}
