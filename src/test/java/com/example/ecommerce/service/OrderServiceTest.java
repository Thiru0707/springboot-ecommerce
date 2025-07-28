
package com.example.ecommerce.service;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock private CartRepository cartRepo;
    @Mock private OrderRepository orderRepo;
    @Mock private OrderItemRepository orderItemRepo;
    @Mock private CartItemRepository cartItemRepo;
    @Mock private UserService userService;

    @InjectMocks private OrderService orderService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrderSuccess() {
        User user = new User();
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(500));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(List.of(item));

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepo.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order order = orderService.placeOrder(user, "pay123");

        assertEquals(BigDecimal.valueOf(1000), order.getTotalAmount());
        verify(orderItemRepo).saveAll(anyList());
        verify(cartItemRepo).deleteAll(cart.getCartItems());
    }

    @Test
    void testPlaceOrderEmptyCart() {
        User user = new User();
        Cart cart = new Cart();
        cart.setCartItems(Collections.emptyList());

        when(cartRepo.findByUser(user)).thenReturn(Optional.of(cart));

        Exception ex = assertThrows(RuntimeException.class, () ->
            orderService.placeOrder(user, "pay123"));

        assertEquals("Cart is empty", ex.getMessage());
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOrdersByUser() {
        User user = new User();
        Order order = new Order();
        when(orderRepo.findByUser(user)).thenReturn(List.of(order));

        List<Order> orders = orderService.getOrders(user);
        assertEquals(1, orders.size());
    }
}
