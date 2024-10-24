package com.diachenko.backend.core.services;
/*  E-commerce-shop
    16.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.dtos.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OrderStatusServiceImplTest {

    UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
    OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0);
    OrderDto orderDtoUpdated = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CREATED, 0);
    @Mock
    private OrderServiceImpl orderServiceImpl;
    @InjectMocks
    private OrderStatusServiceImpl orderStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckoutOrderByUserId() {
        when(orderServiceImpl.getCartOrderDtoByUserId(1L)).thenReturn(orderDto);
        when(orderServiceImpl.getOrderDtoById(1L)).thenReturn(orderDto);
        when(orderServiceImpl.updateOrder(1L, orderDto)).thenReturn(orderDtoUpdated);
        when(orderStatusService.changeStatusOrderByOrderId(1L, String.valueOf(OrderStatus.CREATED))).thenReturn(orderDtoUpdated);

        assertEquals(orderStatusService.checkoutOrderByUserId(1L), orderDtoUpdated);
        assertEquals(OrderStatus.CREATED, orderStatusService.checkoutOrderByUserId(1L).getStatus());
    }

    @Test
    void testChangeStatusOrderByOrderId() {
        when(orderServiceImpl.getOrderDtoById(1L)).thenReturn(orderDto);
        when(orderServiceImpl.updateOrder(1L, orderDto)).thenReturn(orderDtoUpdated);

        assertEquals(orderStatusService.changeStatusOrderByOrderId(1L, "CART"), orderDtoUpdated);
    }

}
