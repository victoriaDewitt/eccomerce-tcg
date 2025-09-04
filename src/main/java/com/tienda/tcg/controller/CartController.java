package com.tienda.tcg.controller;

import com.tienda.tcg.model.CartItem;
import com.tienda.tcg.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Obtener todos los items del carrito de un usuario
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Integer userId) {
        // Cambiado para usar el método correcto en CartService
        return cartService.getCartItems(userId);
    }

    // Agregar un producto al carrito
    @PostMapping("/add")
    public void addToCart(@RequestParam Integer userId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        cartService.addToCart(userId, productId, quantity);
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Integer userId,
            @RequestParam Integer productId) {
        cartService.removeFromCart(userId, productId);
    }

    // Vaciar el carrito
    @DeleteMapping("/clear/{userId}")
    public void clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
    }
}
