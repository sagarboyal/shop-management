package com.main.app.serviceImpl;

import com.main.app.dto.CategoryDTO;
import com.main.app.exception.custom.APIExceptions;
import com.main.app.models.Category;
import com.main.app.payload.response.PagedResponse;
import com.main.app.repository.CategoryRepository;
import com.main.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO saveCategory(CategoryDTO category) {
        if(categoryRepository.findByName(category.getName()).isPresent())
            throw new APIExceptions("Category already exists", HttpStatus.CONFLICT);
        Category data = new Category(category.getName());
        data = categoryRepository.save(data);
        return CategoryDTO.builder()
                .id(data.getId())
                .name(data.getName())
                .build();
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO category) {
        if (categoryRepository.findById(category.getId()).isEmpty())
            throw new APIExceptions("Category doesn't exist", HttpStatus.NOT_FOUND);
        if(categoryRepository.findByName(category.getName()).isPresent())
            throw new APIExceptions("Category already exists", HttpStatus.CONFLICT);
        Category data = categoryRepository.findById(category.getId()).get();
        data.setName(category.getName());
        data = categoryRepository.save(data);
        return CategoryDTO.builder()
                .id(data.getId())
                .name(data.getName())
                .build();
    }

    @Override
    public String deleteCategory(Long categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty())
            throw new APIExceptions("Category doesn't exist",  HttpStatus.NOT_FOUND);
        Category data = categoryRepository.findById(categoryId).get();
        categoryRepository.delete(data);
        return "category with name: " + data.getName().toLowerCase() + " deleted";
    }

    @Override
    public PagedResponse<CategoryDTO> findAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categoryList = categoryPage.getContent();

        if (categoryList.isEmpty()) throw new APIExceptions("empty list", HttpStatus.NO_CONTENT);
        List<CategoryDTO> response = categoryList.stream()
                .map(category -> CategoryDTO.builder().id(category.getId()).name(category.getName()).build())
                .toList();

        return PagedResponse.<CategoryDTO>builder()
                .content(response)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();
    }
}
