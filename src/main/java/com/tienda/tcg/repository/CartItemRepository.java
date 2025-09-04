package com.tienda.tcg.repository;

import com.tienda.tcg.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    // Buscar todos los items del carrito de un usuario específico
    List<CartItem> findByUserId(Integer userId);

    // Opcional: buscar un item específico de un producto para un usuario
    CartItem findByUserIdAndProductId(Integer userId, Integer productId);
}
