package com.example.sportshop.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CUSTOMER_ORDER")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Valid
    @NotNull
    private Address address;

    @NotNull(message = "Name cant be empty!")
    @NotBlank(message = "Name cant be blank!")
    private String fullName;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
