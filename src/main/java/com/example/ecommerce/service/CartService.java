package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final UserService userService;

    // ✅ Get cart for current user
    public Cart getCartForCurrentUser() {
        User user = userService.getCurrentUser();
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> createCartForUser(user));
        recalculateTotal(cart);
        return cart;
    }

    // ✅ Create new cart if none exists
    private Cart createCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotal(BigDecimal.ZERO);
        return cartRepo.save(cart);
    }

    // ✅ Add item to cart by user email
    public CartItem addToCart(String email, Long productId, int quantity) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Cart cart = cartRepo.findByUser(user).orElseGet(() -> createCartForUser(user));
        Product product = productRepo.findById(productId).orElseThrow();

        Optional<CartItem> optionalItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        CartItem item = optionalItem.orElseGet(() -> {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(0);
            cart.getCartItems().add(newItem);
            return newItem;
        });

        item.setQuantity(item.getQuantity() + quantity);
        CartItem savedItem = cartItemRepo.save(item);
        recalculateTotal(cart);
        return savedItem;
    }

    // ✅ Add item to cart for current user
    public CartItem addToCart(Long productId, int quantity) {
        return addToCart(userService.getCurrentUser().getEmail(), productId, quantity);
    }

    // ✅ Update quantity
    public void updateQuantity(Long productId, int quantity) {
        Cart cart = getCartForCurrentUser();
        cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    cartItemRepo.save(item);
                    recalculateTotal(cart);
                });
    }

    // ✅ Remove from cart
    public void removeFromCart(Long productId) {
        Cart cart = getCartForCurrentUser();
        cart.getCartItems().removeIf(item -> {
            if (item.getProduct().getId().equals(productId)) {
                cartItemRepo.delete(item);
                return true;
            }
            return false;
        });
        recalculateTotal(cart);
    }

    // ✅ Get user's cart items by email
    public List<CartItem> getUserCart(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        return cartRepo.findByUser(user).orElseThrow().getCartItems();
    }

    // ✅ Clear cart
    public void clearCart(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Cart cart = cartRepo.findByUser(user).orElseThrow();
        cartItemRepo.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        cartRepo.save(cart);
    }

    // ✅ Recalculate cart total
    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotal(total);
        cartRepo.save(cart);
    }

    // ✅ Get current user email
    public String getCurrentUserEmail() {
        return userService.getCurrentUser().getEmail();
    }
}

