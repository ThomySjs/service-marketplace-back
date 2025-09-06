package com.servicemarketplace.api.services.impl;

import java.util.List;
import java.util.Optional;

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

    public void validateTitle(String title) {
        //Valida el titulo
        if (title == null || title.strip().length() < 5) {
            throw new IllegalArgumentException("El titulo debe contener un minimo de 5 caracteres no vacios.");
        }
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryRequest) {

        //Valida el titulo
        validateTitle(categoryRequest.title());

        Category category = Category.builder()
            .title(categoryRequest.title())
            .description(categoryRequest.description())
            .build();
        Category savedCategory = categoryRepository.save(category);

        return new CategoryDTO(
            savedCategory.getId(),
            savedCategory.getTitle(),
            savedCategory.getDescription());
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAllNotDeleted();
    }

    @Override
    public void deleteById(Long id){
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryRequest) {

        if (categoryRequest.id() == null) {
            throw new IllegalArgumentException("El id de la categoria es obligatoria.");
        }
        validateTitle(categoryRequest.title());
        Optional<Category> foundCategory = categoryRepository.findByIdNotDeleted(categoryRequest.id());

        if (foundCategory.isEmpty()) {
            throw new CategoryNotFoundException("La categoria no existe.");
        }

        Category category = foundCategory.get();
        category.setTitle(categoryRequest.title());
        category.setDescription(categoryRequest.description());

        categoryRepository.save(category);

        return categoryRequest;
    }

}
