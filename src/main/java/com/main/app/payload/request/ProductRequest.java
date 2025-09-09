package com.main.app.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name should not be empty")
    private String name;

    private String code;
    private String description;
    private Double buyPrice;

    @NotNull(message = "Sell price is required")
    @Positive(message = "Sell price must be greater than 0")
    private Double sellPrice;

    @NotNull(message = "Product quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Product category is required")
    private Long categoryId;
}

