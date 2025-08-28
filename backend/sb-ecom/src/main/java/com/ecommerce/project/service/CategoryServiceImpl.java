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
        List<Category> categories=categoryRepository.findAll();

        return categories;
    }

    @Override
    public String addCategory(Category category) {
        Category savedCategory = categoryRepository.save(category);
        return savedCategory.getCategoryId()>1?"Category added successfully":"Something went wrong!!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        List<Category> categories=categoryRepository.findAll();
        Optional<Category> savedCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst();
        System.out.println(category.getCategoryName());
        if(savedCategory.isPresent()){
            Category category1 = savedCategory.get();
            category1.setCategoryName(category.getCategoryName());
            categoryRepository.save(category1);
            return category1;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @Override
    public String deleteCategory(Long categoryId) {
        List<Category> categories=categoryRepository.findAll();
        Category unwantedCategory = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        categoryRepository.delete(unwantedCategory);
        return "Category with categoryId: " + categoryId + " deleted successfully !!";
    }
}
