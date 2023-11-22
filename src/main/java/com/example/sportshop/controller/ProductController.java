package com.example.sportshop.controller;

import com.example.sportshop.entity.Product;
import com.example.sportshop.entity.ProductType;
import com.example.sportshop.repo.ProductRepository;
import com.example.sportshop.service.ProductSessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductSessionHelper productSessionHelper;

    @Autowired
    public ProductController(ProductRepository productRepository, ProductSessionHelper productSessionHelper) {
        this.productRepository = productRepository;
        this.productSessionHelper = productSessionHelper;
    }

    @GetMapping("/all")
    public String getAll(Model model, @RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(defaultValue = "3") int size) {
        try {
            List<Product> products;
            Pageable paging = PageRequest.of(page - 1, size);
            Page<Product> pageProducts;

            if(keyword == null) {
                pageProducts = productRepository.findAll(paging);
            } else {
                pageProducts = productRepository.findAllByProductType(Enum.valueOf(ProductType.class, keyword), paging);
                model.addAttribute("keyword", keyword);
            }

           ProductSessionHelper.TotalAmountAndPrice totalAmountAndPrice =
                   productSessionHelper.getTotalAmountOfItemsAndTotalPrice();

            System.out.println(totalAmountAndPrice);

            products = pageProducts.getContent();

            model.addAttribute("totalPrice", totalAmountAndPrice.getTotalPrice());
            model.addAttribute("totalAmount", totalAmountAndPrice.getTotalAmount());
            model.addAttribute("products", products);
            model.addAttribute("currentPage", pageProducts.getNumber() + 1);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("pageSize", size);
        }catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "products";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam long productId) {
        Optional<Product> optional = productRepository.findProductById(productId);
        Product product;

        if (optional.isEmpty())
            return "redirect:/all";
        else product = optional.get();

        productSessionHelper.setProduct(product);
        return "redirect:/all";
    }
}
