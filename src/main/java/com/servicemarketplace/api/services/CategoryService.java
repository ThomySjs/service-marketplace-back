package com.servicemarketplace.api.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.dto.CategoryDTO;

public interface CategoryService {

    Category getCategoryById(Long id);

    CategoryDTO create(CategoryDTO categoryRequest);

    List<CategoryDTO> getAll();

    Page<CategoryDTO> getPage(Pageable pageable);

    void deleteById(Long id);

    CategoryDTO update(CategoryDTO categoryRequest);
}
