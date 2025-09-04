package com.tienda.tcg.repository;

import com.tienda.tcg.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Este método busca productos por categoría
    List<Product> findByCategory(String category);

    List<Product> findByExpansion(String expansion);

    @Query("SELECT p FROM Product p WHERE LOWER(p.expansion) = LOWER(:expansion)")
    List<Product> findByExpansionIgnoreCase(@Param("expansion") String expansion);

}
