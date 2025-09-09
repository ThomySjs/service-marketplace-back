package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.dto.CategoryDTO;

public interface CategoryService {

    Category getCategoryById(Long id);

    CategoryDTO create(CategoryDTO categoryRequest);

    List<CategoryDTO> getAll();

    void deleteById(Long id);

    CategoryDTO update(CategoryDTO categoryRequest);
}
