package com.diachenko.backend.application.services;
/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.dtos.OrderDto;
import org.springframework.data.domain.Page;


public interface OrderService {

    Page<OrderDto> getAllOrders(int page, int size);

    Page<OrderDto> getAllOrdersDtoByUserId(Long userId, int page, int size);

    Page<OrderDto> getAllOrdersWithStatus(OrderStatus orderStatus, int page, int size);

    Order createEmptyCart(Long userId);

    OrderDto getCartOrderDtoByUserId(Long userId);

    Order getOrderById(Long orderId);

    OrderDto getOrderDtoById(Long orderId);

    OrderDto updateOrder(Long id, OrderDto orderDto);

}
