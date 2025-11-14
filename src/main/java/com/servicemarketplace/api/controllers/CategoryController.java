package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.CategoryDTO;
import com.servicemarketplace.api.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Agrega una categoria.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Validated @RequestBody CategoryDTO request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @Operation(summary = "Obtiene todas las categorias.")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Operation(summary = "Obtiene todas las categorias con paginacion.")
    @GetMapping("/categories/paginated")
    public ResponseEntity<?> getAllCategoriesByPage(Pageable pageable) {
        return ResponseEntity.ok(categoryService.getPage(pageable));
    }

    @Operation(summary = "Elimina una categoria.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edita una categoria.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryRequest) {
        return ResponseEntity.ok(categoryService.update(categoryRequest));
    }

}
