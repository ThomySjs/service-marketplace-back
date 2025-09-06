package com.servicemarketplace.api.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.servicemarketplace.api.domain.entities.Category;
import com.servicemarketplace.api.domain.repositories.CategoryRepository;
import com.servicemarketplace.api.dto.CategoryDTO;
import com.servicemarketplace.api.exceptions.auth.CategoryNotFoundException;
import com.servicemarketplace.api.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse create(CategoryRequest categoryRequest) {

        //Valida el titulo
        if (categoryRequest.title().strip().length() < 5) {
            throw new IllegalArgumentException("El titulo debe contener un minimo de 5 caracteres no vacios.");
        }

        Category category = Category.builder()
            .title(categoryRequest.title())
            .description(categoryRequest.description())
            .build();
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
            savedCategory.getId(),
            savedCategory.getTitle(),
            savedCategory.getDescription());
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAllCustom();
    }

    @Override
    public void deleteById(Long id){
        categoryRepository.deleteById(id);
    }

}
