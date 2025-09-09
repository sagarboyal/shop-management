package com.main.app.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductUpdateRequest {
    @NotNull(message = "product id is required")
    private Long id;
    private String name;
    private String code;
    private String description;
    private Double buyPrice;
    private Double sellPrice;
    private Integer quantity;
    private Long categoryId;
}
