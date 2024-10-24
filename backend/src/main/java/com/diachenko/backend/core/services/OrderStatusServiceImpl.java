package com.diachenko.backend.core.services;
/*  E-commerce-shop
    13.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.OrderStatusService;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.dtos.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderServiceImpl orderServiceImpl;

    @Override
    public OrderDto checkoutOrderByUserId(Long userId) {
        OrderDto orderDto = orderServiceImpl.getCartOrderDtoByUserId(userId);
        return changeStatusOrderByOrderId(orderDto.getId(), String.valueOf(OrderStatus.CREATED));
    }

    @Override
    public OrderDto changeStatusOrderByOrderId(Long orderId, String status) {
        OrderDto orderDto = orderServiceImpl.getOrderDtoById(orderId);
        orderDto.setStatus(OrderStatus.valueOf(status));
        return orderServiceImpl.updateOrder(orderId, orderDto);
    }
}
