package com.diachenko.backend.application.services;
/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.dtos.OrderDto;

import java.util.List;


public interface OrderService {


    List<OrderDto> getAllOrders();

    List<OrderDto> getAllOrdersWithStatus(OrderStatus orderStatus);

    List<OrderDto> getAllOrdersDtoByUserId(Long userId);

    Order createEmptyCart(Long userId);

    OrderDto getCartOrderDtoByUserId(Long userId);

    Order getOrderById(Long orderId);

    OrderDto getOrderDtoById(Long orderId);

    OrderDto updateOrder(Long id, OrderDto orderDto);

}
