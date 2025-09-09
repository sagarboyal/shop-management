package com.main.app.payload.response;

import com.main.app.models.Product;
import java.time.LocalDateTime;

public record ProductResponse(
        Long  id,
        String name,
        String code,
        String description,
        Double buyPrice,
        Double sellPrice,
        Integer quantity,
        String category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse fromEntity(Product p, String categoryName) {
        return new ProductResponse(p.getId(), p.getName(), p.getCode(), p.getDescription(), p.getBuyPrice(), p.getSellPrice(), p.getQuantity(), categoryName, p.getCreatedAt(), p.getUpdatedAt());
    }
}
