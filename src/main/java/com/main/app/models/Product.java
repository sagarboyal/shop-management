package com.main.app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private String code;
    private String description;
    private Double buyPrice;
    private Double sellPrice;
    private Integer quantity;

    @ManyToOne
    private Category category;

    public Product(Long id, String name, String code, String description, Double buyPrice, Double sellPrice, Integer quantity, Category category) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
        this.category = category;
    }

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
