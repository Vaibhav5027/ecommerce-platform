package com.ecommerce.project.repository;

import com.ecommerce.project.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryName(String name);

  Optional<Category> findByCategoryId(Long categoryId);
}
