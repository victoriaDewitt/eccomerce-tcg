package com.tienda.tcg.controller;

import com.tienda.tcg.model.Product;
import com.tienda.tcg.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Obtener todos los productos
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Obtener productos por categoría
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productRepository.findByCategory(category);
    }

    // Obtener productos por expansión (ignora mayúsculas/minúsculas)
    @GetMapping("/expansion/{expansion}")
    public List<Product> getProductsByExpansion(@PathVariable String expansion) {
        return productRepository.findByExpansionIgnoreCase(expansion);
    }
}
