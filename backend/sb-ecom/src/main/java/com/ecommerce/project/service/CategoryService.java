package com.ecommerce.project.service;



import com.ecommerce.project.model.Category;


import java.util.List;


public interface CategoryService {

    List<Category> getAllCategories();

    String addCategory(Category category);

    Category updateCategory(Category category,Long categoryId);

    String deleteCategory(Long categoryId);
}
