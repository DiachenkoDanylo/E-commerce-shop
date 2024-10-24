package com.diachenko.backend.application.services;

import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.dtos.OrderItemDto;

import java.util.List;

/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

public interface OrderItemService {

    OrderItem addToCurrentCartItem(Long userId, Long itemId, Integer quantity);

    List<OrderItemDto> getOrderItemList(Long id);

    List<OrderItemDto> update(Long id, List<OrderItemDto> orderItemDtos);
}
