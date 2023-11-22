package com.example.sportshop.controller;

import com.example.sportshop.entity.Order;
import com.example.sportshop.entity.OrderItem;
import com.example.sportshop.entity.Product;
import com.example.sportshop.repo.OrderRepository;
import com.example.sportshop.repo.ProductRepository;
import com.example.sportshop.service.ProductSessionHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ProductSessionHelper productSessionHelper;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CartController(ProductSessionHelper productSessionHelper, ProductRepository productRepository,
                          OrderRepository orderRepository) {
        this.productSessionHelper = productSessionHelper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String showCart(Model model) {
        Map<Product, Integer> productCart = productSessionHelper.getCartMap();
        ProductSessionHelper.TotalAmountAndPrice totalAmountAndPrice = productSessionHelper
                .getTotalAmountOfItemsAndTotalPrice();

        model.addAttribute("totalPrice", totalAmountAndPrice.getTotalPrice());
        model.addAttribute("totalAmount", totalAmountAndPrice.getTotalAmount());
        model.addAttribute("productCart", productCart);

        return "cartForm";
    }

    @GetMapping("/checkout")
    public String createOrder(Model model) {
        if (productSessionHelper.getCartMap() == null)
            return "redirect:/all";

        Order order = new Order();
        model.addAttribute("order", order);

        return "checkOutForm";
    }

    @PostMapping("/checkout")
    public String checkOut(@Valid Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "checkOutForm";

        Map<Product, Integer> productCart = productSessionHelper.getCartMap();

        for (Map.Entry<Product, Integer> entry : productCart.entrySet()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(entry.getKey());
            orderItem.setQuantity(entry.getValue());
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        productSessionHelper.clearCart();
        return "redirect:/all";
    }

    @PostMapping("/remove")
    public String removeProduct(@RequestParam(value = "productId") Long productId) {
        Optional<Product> productOptional = productRepository.findProductById(productId);
        Product product;
        if (productOptional.isEmpty())
            return "redirect:/cart";
        else product = productOptional.get();

        productSessionHelper.removeProduct(product);
        return "redirect:/cart";
    }
}
