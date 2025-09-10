package com.ecommerce.project.service;



import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.model.Category;


public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String orderBy);

    CategoryDTO addCategory(Category category);

    CategoryDTO updateCategory(Category category, Long categoryId);

    CategoryDTO deleteCategory(Long categoryId);
}
