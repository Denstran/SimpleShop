package com.example.sportshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PRODUCT")
@Setter
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String productName;

    @Size(min = 1, max = 255)
    @NotNull
    @NotBlank
    private String description;

    private double productPrice;

    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                '}';
    }
}
