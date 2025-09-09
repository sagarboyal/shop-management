package com.main.app.service;

import com.main.app.payload.request.ProductRequest;
import com.main.app.payload.request.ProductUpdateRequest;
import com.main.app.payload.response.PagedResponse;
import com.main.app.payload.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(ProductUpdateRequest productRequest);
    String deleteProduct(Long productId);
    PagedResponse<ProductResponse> findAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    ProductResponse findProductById(Long productId);
}
