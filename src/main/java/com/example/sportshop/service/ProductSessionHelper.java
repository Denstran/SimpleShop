package com.example.sportshop.service;

import com.example.sportshop.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductSessionHelper {
    private final HttpSession httpSession;

    @Autowired
    public ProductSessionHelper(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Map<Product, Integer> getCartMap() {
        setMapToSessionIfAbsent();

        return (Map<Product, Integer>) httpSession.getAttribute("productMap");
    }

    public void removeProduct(Product product) {
        Map<Product, Integer> cartMap = getCartMap();
        cartMap.remove(product);
    }

    public void setProduct(Product product) {
        Map<Product, Integer> productMap = getCartMap();

        int quantityOfProducts = productMap.getOrDefault(product, 1);

        if (productMap.containsKey(product))
            productMap.put(product, ++quantityOfProducts);
        else productMap.put(product, quantityOfProducts);
    }

    private void setMapToSessionIfAbsent() {
        if (httpSession.getAttribute("productMap") == null) {
            Map<Product, Integer> productMap = new HashMap<>();
            httpSession.setAttribute("productMap", productMap);
        }
    }

    public TotalAmountAndPrice getTotalAmountOfItemsAndTotalPrice() {
        Map<Product, Integer> map = getCartMap();

        int quantityOfProductInTheCart = 0;
        double totalPrice = 0;

        for (Map.Entry<Product, Integer> entry : map.entrySet()) {
            quantityOfProductInTheCart += entry.getValue();
            totalPrice = totalPrice + entry.getKey().getProductPrice() * entry.getValue();
        }

        return new TotalAmountAndPrice(quantityOfProductInTheCart, totalPrice);
    }

    public void clearCart() {
        Map<Product, Integer> map = (Map<Product, Integer>) httpSession.getAttribute("productMap");
        if (map == null) {
            httpSession.setAttribute("productMap", map);
        } else map.clear();
    }

    @Data
    public static class TotalAmountAndPrice {
        private final int totalAmount;
        private final double totalPrice;
    }
}
