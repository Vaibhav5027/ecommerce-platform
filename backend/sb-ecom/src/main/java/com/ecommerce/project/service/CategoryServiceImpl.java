package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;


import java.util.List;
import java.util.Optional;

import com.ecommerce.project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public String addCategory(Category category) {
        try {
            Category savedCategory = categoryRepository.save(category);

            if (savedCategory.getCategoryId() != null) {
                return "Category added successfully";
            } else {
                return "Category could not be saved!";
            }
        } catch (Exception e) {
            return "Something went wrong while saving category!";
        }
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        savedCategory.setCategoryName(category.getCategoryName());
        Category save = categoryRepository.save(savedCategory);
        return save;
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories = categoryRepository.findAll();
        Category unwantedCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        categoryRepository.delete(unwantedCategory);
        return "Category with categoryId: " + categoryId + " deleted successfully !!";
    }
}
