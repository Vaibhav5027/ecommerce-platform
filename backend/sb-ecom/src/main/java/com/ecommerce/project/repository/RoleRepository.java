package com.ecommerce.project.repository;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long> {
  Optional<Roles> findByRoleName(AppRole appRole);
}
