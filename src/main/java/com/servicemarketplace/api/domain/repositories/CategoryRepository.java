package com.servicemarketplace.api.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.dto.CategoryDTO;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.servicemarketplace.api.dto.CategoryDTO(c.id, c.title, c.description) " +
       "FROM categories c " +
       "WHERE c.deleted = false " +
       "ORDER BY c.title")
    List<CategoryResponse> findAllCustom();
}
