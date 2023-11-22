package com.example.sportshop.repo;

import com.example.sportshop.entity.Product;
import com.example.sportshop.entity.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findAllByProductType(ProductType productType, Pageable pageable);
    Optional<Product> findProductById(Long id);
}
