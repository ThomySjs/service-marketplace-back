package com.servicemarketplace.api.services;

import java.util.List;

import com.servicemarketplace.api.dto.CategoryRequest;
import com.servicemarketplace.api.dto.CategoryResponse;

public interface CategoryService {
    CategoryResponse create(CategoryRequest categoryRequest);

    List<CategoryResponse> getAll();
}
