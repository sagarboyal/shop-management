package com.main.app.service;

import com.main.app.dto.CategoryDTO;
import com.main.app.payload.response.PagedResponse;

public interface CategoryService {
    CategoryDTO saveCategory(CategoryDTO category);
    CategoryDTO updateCategory(CategoryDTO category);
    String deleteCategory(Long categoryId);
    PagedResponse<CategoryDTO> findAllCategories( Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
