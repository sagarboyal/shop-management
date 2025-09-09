package com.main.app.controller;

import com.main.app.constraint.AppConstraint;
import com.main.app.payload.request.ProductRequest;
import com.main.app.payload.request.ProductUpdateRequest;
import com.main.app.payload.response.PagedResponse;
import com.main.app.payload.response.ProductResponse;
import com.main.app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> productHandler(
            @RequestParam(name = "pageNumber", defaultValue = AppConstraint.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraint.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraint.DEFAULT_SORT_BY_PRODUCT, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraint.SORT_DIR, required = false) String sortOrder
    ){
        return ResponseEntity.ok(productService.findAllProducts(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductUpdateRequest productRequest){
        return ResponseEntity.ok(productService.updateProduct(productRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
