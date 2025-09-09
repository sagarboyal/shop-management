package com.main.app.controller;

import com.main.app.constraint.AppConstraint;
import com.main.app.dto.CategoryDTO;
import com.main.app.payload.response.PagedResponse;
import com.main.app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PagedResponse<CategoryDTO>> categoryHandler(
            @RequestParam(name = "pageNumber", defaultValue = AppConstraint.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraint.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraint.DEFAULT_SORT_BY_CATEGORY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraint.SORT_DIR, required = false) String sortOrder
    ){
        return ResponseEntity.ok(categoryService.findAllCategories(pageNumber, pageSize, sortBy, sortOrder));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO dto){
        return ResponseEntity.ok(categoryService.saveCategory(dto));
    }

    @PutMapping
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO dto){
        return ResponseEntity.ok(categoryService.updateCategory(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }
}
