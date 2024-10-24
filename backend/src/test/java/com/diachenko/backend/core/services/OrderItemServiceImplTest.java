package com.diachenko.backend.core.services;
/*  E-commerce-shop
    17.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.mappers.OrderMapper;
import com.diachenko.backend.infrastructure.repositories.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

class OrderItemServiceImplTest {

    @Mock
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private ItemServiceImpl itemServiceImpl;

    @Mock
    private InventoryServiceImpl inventoryServiceImpl;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCurrentCartItem() {
        when(inventoryServiceImpl.isAvailible(1L,15)).thenReturn(true);

    }
//
//    @Override
//    public OrderItem addToCurrentCartItem(Long userId, Long itemId, Integer quantity) {
//        if (inventoryServiceImpl.isAvailible(itemId, quantity)) {
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(orderMapper.toOrder(orderServiceImpl.getCartOrderDtoByUserId(userId)));
//            inventoryServiceImpl.changeItemQuantity(itemId, quantity);
//            orderItem.setItem(itemMapper.toItem(itemServiceImpl.getItemDto(itemId)));
//            orderItem.setQuantity(quantity);
//            orderItem.setTotalPrice(itemServiceImpl.getItemDto(itemId).getBasePrice() * quantity);
//            return orderItemRepository.save(orderItem);
//        } else {
//            throw new AppException("Quantity of item not availible", HttpStatus.BAD_REQUEST);
//        }
//    }
}
