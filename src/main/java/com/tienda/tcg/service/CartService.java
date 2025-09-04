package com.tienda.tcg.service;

import com.tienda.tcg.model.CartItem;
import com.tienda.tcg.model.Product;
import com.tienda.tcg.repository.CartItemRepository;
import com.tienda.tcg.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // Obtener todos los items del carrito de un usuario
    public List<CartItem> getCartItems(Integer userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // Agregar producto al carrito
    public void addToCart(Integer userId, Integer productId, Integer quantity) {
        // Convertir Integer a Long usando Long.valueOf()
        Optional<Product> productOpt = productRepository.findById(Long.valueOf(productId));
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        Product product = productOpt.get();

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);
    }

    // Eliminar producto del carrito
    public void removeFromCart(Integer userId, Integer productId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(Long.valueOf(productId))) {
                cartItemRepository.delete(item);
                break;
            }
        }
    }

    // Vaciar carrito de un usuario
    public void clearCart(Integer userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(items);
    }
}
