package com.ecommerce.project.service.category;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.category.CategoryDTO;
import com.ecommerce.project.payload.category.CategoryResponse;

public interface CategoryService {

  CategoryResponse getAllCategories(
      Integer pageNumber, Integer pageSize, String sortBy, String orderBy);

  CategoryDTO addCategory(Category category);

  CategoryDTO updateCategory(Category category, Long categoryId);

  CategoryDTO deleteCategory(Long categoryId);
}
