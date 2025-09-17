package com.ecommerce.project.service.category;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;


import java.util.List;
import java.util.Optional;


import com.ecommerce.project.payload.category.CategoryDTO;
import com.ecommerce.project.payload.category.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
     ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String orderBy) {
         Sort sort =orderBy.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> categoryPage  = categoryRepository.findAll(pageDetails);
        List<CategoryDTO> list = categoryPage.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
       CategoryResponse categoryResponse=new CategoryResponse();
       categoryResponse.setContent(list);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(Category category) {
        Optional<Category> byCategoryName = categoryRepository.findByCategoryName(category.getCategoryName());
        if(byCategoryName.isPresent())
            throw new APIException("category with this name is already present");

        Category savedCategory = categoryRepository.save(category);

      return modelMapper.map(savedCategory, CategoryDTO.class);

    }


    @Override
    public CategoryDTO updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        savedCategory.setCategoryName(category.getCategoryName());
        savedCategory = categoryRepository.save(savedCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> byId = categoryRepository.findById(categoryId);
        if(byId.isEmpty())
            throw new APIException("cannot find id with : "+categoryId);
        CategoryDTO deleted = modelMapper.map(byId.get(), CategoryDTO.class);
        categoryRepository.delete(byId.get());
        return deleted;
    }
}
