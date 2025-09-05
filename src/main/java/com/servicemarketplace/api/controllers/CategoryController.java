package com.servicemarketplace.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.servicemarketplace.api.dto.CategoryRequest;
import com.servicemarketplace.api.dto.CategoryResponse;
import com.servicemarketplace.api.services.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> add(@Validated @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

}
