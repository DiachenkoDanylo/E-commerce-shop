package com.diachenko.backend.core.services;
/*  E-commerce-shop
    17.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.*;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.dtos.OrderItemDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.mappers.OrderItemMapper;
import com.diachenko.backend.infrastructure.mappers.OrderMapper;
import com.diachenko.backend.infrastructure.repositories.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class OrderItemServiceImplTest {

    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE);
    Category category = new Category(1L, "testing category", "testing description");
    Item item1 = new Item(2L, "testItem1", category, "testDesc1", 100, null, 10);
    ItemDto item1Dto = new ItemDto(2L, "testItem1", 1L, "testDesc1", 100, null, 10);
    List<OrderDto> orderDtos = new ArrayList<>();
    User user = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
    UserDto userDto = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
    Order order = new Order(1L, new ArrayList<>(), user, OrderStatus.CART);
    OrderItem orderItemUpdated = new OrderItem(1L, order, item1, 1, 100);
    OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), userDto, OrderStatus.CART, 0);
    OrderItemDto orderItemDto = new OrderItemDto(1L, item1Dto, 1, 100);
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
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCurrentCartItem_AppException() {
        when(inventoryServiceImpl.isAvailible(1L, 15)).thenReturn(false);

        AppException thrown = assertThrows(AppException.class, () -> {
            orderItemService.addToCurrentCartItem(1L, 2L, 2);
        });

        assertEquals("Quantity of item not availible", thrown.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatus());
    }

    @Test
    void testAddToCurrentCartItem() {
        orderDtos.add(orderDto);
        order.addItem(orderItemUpdated);

        when(inventoryServiceImpl.isAvailible(2L, 1)).thenReturn(true);
        when(orderServiceImpl.getCartOrderDtoByUserId(1L)).thenReturn(orderDto);
        when(orderMapper.toOrder(orderDto)).thenReturn(order);
        when(itemServiceImpl.getItemDto(2L)).thenReturn(item1Dto);
        when(itemMapper.toItem(any(), any())).thenReturn(item1);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItemUpdated);

        assertEquals(orderItemService.addToCurrentCartItem(1L, 2L, 1), orderItemUpdated);
    }

    @Test
    void testGetOrderItemList() {
        Page<OrderItem> page = new PageImpl<>(List.of(orderItemUpdated), PAGEABLE, 1);
        Page<OrderItemDto> pageDto = new PageImpl<>(List.of(orderItemDto), PAGEABLE, 1);
        when(orderItemRepository.findOrderItemsByOrderId(1L, PAGEABLE)).thenReturn(page);
        when(orderItemMapper.toOrderItemDtoList(List.of(orderItemUpdated))).thenReturn(List.of(orderItemDto));

        assertEquals(orderItemService.getOrderItemList(1L, PAGE, SIZE), pageDto);
    }

    @Test
    void testUpdate() {
        orderDtos.add(orderDto);
        order.addItem(orderItemUpdated);
        Page<OrderItem> page = new PageImpl<>(List.of(orderItemUpdated), PAGEABLE, 1);

        when(orderItemRepository.findOrderItemsByOrderId(1L,PAGEABLE)).thenReturn(page);
        when(orderItemMapper.toOrderItemList(anyList())).thenReturn(List.of(orderItemUpdated));
        when(orderItemRepository.saveAll(List.of(orderItemUpdated))).thenReturn(List.of(orderItemUpdated));
        when(orderItemMapper.toOrderItemDtoList(anyList())).thenReturn(List.of(orderItemDto));

        assertEquals(orderItemService.update(1L, List.of(orderItemDto)), List.of(orderItemDto));
    }
}
