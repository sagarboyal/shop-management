package com.main.app.serviceImpl;

import com.main.app.dto.CategoryDTO;
import com.main.app.exception.custom.APIExceptions;
import com.main.app.models.Category;
import com.main.app.models.Product;
import com.main.app.payload.request.ProductRequest;
import com.main.app.payload.request.ProductUpdateRequest;
import com.main.app.payload.response.PagedResponse;
import com.main.app.payload.response.ProductResponse;
import com.main.app.repository.CategoryRepository;
import com.main.app.repository.ProductRepository;
import com.main.app.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        if(productRepository.findByName(productRequest.getName()).isPresent())
            throw new APIExceptions("product with this name already exists", HttpStatus.CONFLICT);
        Category category = findCategory(productRequest.getCategoryId());
        Product product = convertEntity(productRequest, category);
        product = productRepository.save(product);
        return ProductResponse.fromEntity(product, category.getName());
    }

    @Override
    public ProductResponse updateProduct(ProductUpdateRequest productRequest) {
        Product product = findProduct(productRequest.getId());
        if(productRepository.findByName(productRequest.getName()).isPresent())
            throw new APIExceptions("product with this name already exists", HttpStatus.CONFLICT);

        product.setName(productRequest.getName() == null || productRequest.getName().trim().isEmpty() ? product.getName() : productRequest.getName());
        product.setCode(productRequest.getCode() == null || productRequest.getCode().trim().isEmpty() ? product.getCode() : productRequest.getCode());
        product.setDescription(productRequest.getDescription() == null || productRequest.getDescription().trim().isEmpty() ? product.getDescription() : productRequest.getDescription());
        if(productRequest.getBuyPrice() != null) product.setBuyPrice(productRequest.getBuyPrice());
        if(productRequest.getSellPrice()!= null) product.setSellPrice(productRequest.getSellPrice());
        if(productRequest.getQuantity() != null) product.setQuantity(productRequest.getQuantity());
        if(productRequest.getCategoryId() != null) product.setCategory(findCategory(productRequest.getCategoryId()));

        product = productRepository.save(product);
        return ProductResponse.fromEntity(product, product.getCategory().getName());
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
        return "product with id " + productId + " has been deleted";
    }

    @Override
    public PagedResponse<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();


        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> productList = productPage.getContent();

        if (productList.isEmpty()) throw new APIExceptions("empty list", HttpStatus.NO_CONTENT);
        List<ProductResponse> response = productList.stream()
                .map(product ->  ProductResponse.fromEntity(product, product.getCategory().getName()))
                .toList();

        return PagedResponse.<ProductResponse>builder()
                .content(response)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse findProductById(Long productId) {
        Product product = findProduct(productId);
        return ProductResponse.fromEntity(product, product.getCategory().getName());
    }

    private Product convertEntity(ProductRequest productRequest, Category category) {
        return new Product(
          null,
          productRequest.getName(),
          productRequest.getCode(),
          productRequest.getDescription(),
          productRequest.getBuyPrice(),
          productRequest.getSellPrice(),
          productRequest.getQuantity(),
          category
        );
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new APIExceptions("product not found", HttpStatus.NOT_FOUND));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new APIExceptions("category not found", HttpStatus.NOT_FOUND));
    }
}
