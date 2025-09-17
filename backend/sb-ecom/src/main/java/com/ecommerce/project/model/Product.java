package com.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    @Size(min=3 ,message = "Product name should contain least 3 characters")
    private String productName;
    private String description;
    @NotBlank
    private Integer quantity;
    @NotBlank
    private double price;
    private Double specialPrice;
    private Double discount;
    private String image;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


}
