package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderControllerTest {

    @Mock 
    private OrderService orderService;

    @Mock 
    private UserService userService;

    @InjectMocks 
    private OrderController orderController;

    @Test
    void testGetOrders() {
        // Arrange
        User user = new User(); 
        user.setId(1L);
        Order mockOrder = new Order(); 
        mockOrder.setId(100L);

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(orderService.getOrders(user)).thenReturn(List.of(mockOrder));

        // Act
        List<Order> orders = orderController.getOrders();  // ✅ no parameter

        // Assert
        assertEquals(1, orders.size());
        assertEquals(100L, orders.get(0).getId());
    }
}
