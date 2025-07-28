package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceTest {

    @Mock private UserService userService;
    @Mock private OrderRepository orderRepository;

    @InjectMocks private OrderService orderService;

    @Test
    void testGetOrdersForCurrentUser() {
        User user = new User(); user.setId(1L);
        Order order = new Order(); order.setId(100L);

        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrdersForCurrentUser();
        assertEquals(1, orders.size());
        assertEquals(100L, orders.get(0).getId());
    }

    @Test
    void testGetOrderById() {
        Order order = new Order(); order.setId(200L);
        Mockito.when(orderRepository.findById(200L)).thenReturn(java.util.Optional.of(order));

        Order found = orderService.getOrderById(200L);
        assertEquals(200L, found.getId());
    }
}
