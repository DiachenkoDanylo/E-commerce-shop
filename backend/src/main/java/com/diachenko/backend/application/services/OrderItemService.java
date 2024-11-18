package com.diachenko.backend.application.services;

import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.dtos.OrderItemDto;
import org.springframework.data.domain.Page;

import java.util.List;

/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

public interface OrderItemService {

    OrderItem addToCurrentCartItem(Long userId, Long itemId, Integer quantity);

    Page<OrderItemDto> getOrderItemList(Long id, int page, int size);

    List<OrderItemDto> update(Long id, List<OrderItemDto> orderItemDtos);
}
