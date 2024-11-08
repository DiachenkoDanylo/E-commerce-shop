package com.diachenko.backend.core.services;

import com.diachenko.backend.application.services.OrderItemService;
import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.dtos.OrderItemDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.mappers.OrderItemMapper;
import com.diachenko.backend.infrastructure.mappers.OrderMapper;
import com.diachenko.backend.infrastructure.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/
@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderServiceImpl orderServiceImpl;
    private final ItemServiceImpl itemServiceImpl;
    private final InventoryServiceImpl inventoryServiceImpl;
    private final OrderMapper orderMapper;
    private final ItemMapper itemMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final CategoryServiceImpl categoryService;

    @Override
    public OrderItem addToCurrentCartItem(Long userId, Long itemId, Integer quantity) {
        if (inventoryServiceImpl.isAvailible(itemId, quantity)) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orderMapper.toOrder(orderServiceImpl.getCartOrderDtoByUserId(userId)));
            inventoryServiceImpl.changeItemQuantity(itemId, quantity);
            orderItem.setItem(itemMapper.toItem(itemServiceImpl.getItemDto(itemId),categoryService));
            orderItem.setQuantity(quantity);
            orderItem.setTotalPrice(itemServiceImpl.getItemDto(itemId).getBasePrice() * quantity);
            return orderItemRepository.save(orderItem);
        } else {
            throw new AppException("Quantity of item not availible", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<OrderItemDto> getOrderItemList(Long id) {
        return orderItemMapper.toOrderItemDtoList(orderItemRepository.findOrderItemsByOrderId(id));
    }

    @Override
    public List<OrderItemDto> update(Long id, List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrderId(id);
        orderItemMapper.updateOrderItemList(orderItems, orderItemMapper.toOrderItemList(orderItemDtos));
        orderItems.stream().forEach(s -> s.setOrder(orderItemRepository.findOrderItemsByOrderId(id).get(0).getOrder()));
        List<OrderItem> saved = orderItemRepository.saveAll(orderItems);
        saved.stream().forEach(s -> s.setTotalPrice(s.getItem().getBasePrice() * s.getQuantity()));
        return orderItemMapper.toOrderItemDtoList(saved);
    }
}
