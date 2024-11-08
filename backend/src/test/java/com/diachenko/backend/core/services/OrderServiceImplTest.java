package com.diachenko.backend.core.services;
/*  E-commerce-shop
    15.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.OrderMapperImpl;
import com.diachenko.backend.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private OrderMapperImpl orderMapper;

    @BeforeEach
    public void setUp() {
        // Инициализируем моки
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("test CreateEmptyCart")
    void testCreateEmptyCart() {
        // Мокаем данные
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setLogin("Test User");

        when(userServiceImpl.getUserById(1L)).thenReturn(mockUser);

        // Мокаем поведение orderRepository
        Order mockOrder = new Order(mockUser, OrderStatus.CART);
        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(mockOrder);

        // Вызываем метод, который тестируем
        Order createdOrder = orderService.createEmptyCart(1L);

        // Проверяем результат
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.CART, createdOrder.getStatus());
        assertEquals(mockUser, createdOrder.getUser());

        // Проверяем, что userServiceImpl был вызван с правильным параметром
        verify(userServiceImpl, times(1)).getUserById(1L);

        // Проверяем, что orderRepository.saveAndFlush был вызван
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));

    }

    @Test
    @DisplayName("test CreateEmptyCart_AppException")
    void testCreateEmptyCart_AppException() {
        // Мокаем данные
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setLogin("Test User");

        when(userServiceImpl.getUserById(1L)).thenThrow(new AppException("User not Found", HttpStatus.NOT_FOUND));

        // Проверяем, что метод выбрасывает AppException при вызове
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.createEmptyCart(1L); // Вызов метода, который должен выбросить исключение
        });

        // Проверяем сообщение исключения (необязательно)
        assertEquals("User not Found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        // Проверяем, что метод getUserById был вызван с параметром 1L
        verify(userServiceImpl, times(1)).getUserById(1L);

        // Убеждаемся, что метод saveAndFlush не был вызван, так как пользователь не найден
        verify(orderRepository, never()).saveAndFlush(any(Order.class));
    }


    @Test
    void testGetAllOrders() {

        OrderDto orderDto1 = new OrderDto();
        OrderDto orderDto2 = new OrderDto();
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto1);
        orderDtos.add(orderDto2);

        when(orderMapper.toOrderDtoList(anyList())).thenReturn(orderDtos);

        assertEquals(orderDtos, orderService.getAllOrders());
    }

    @Test
    void testGetAllOrdersWithStatus() {

        List<OrderDto> orderDtos = new ArrayList<>();
        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        orderDtos.add(new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0));

        when(orderMapper.toOrderDtoList(anyList())).thenReturn(orderDtos);
        when(orderRepository.findAllByStatus(OrderStatus.CART)).thenReturn(Collections.emptyList());

        assertEquals(orderService.getAllOrdersWithStatus(OrderStatus.CART), orderDtos);
        assertEquals(orderService.getAllOrdersWithStatus(OrderStatus.CART).get(0).getUser().getFirstName(), orderDtos.get(0).getUser().getFirstName());
    }

    @Test
    void testGetAllOrdersDtoByUserId_OrderList_empty() {

        when(orderRepository.findOrderByUserId(1L)).thenReturn(Collections.emptyList());

        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");


        Order orderTest = new Order(null, null, userTest, OrderStatus.CART);

        when(userServiceImpl.getUserById(userTest.getId())).thenReturn(userTest);

        List<OrderDto> orderDtos = new ArrayList<>();
        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        orderDtos.add(new OrderDto(null, null, user, OrderStatus.CART, 0));


        when(orderMapper.toOrderDtoList(anyList())).thenReturn(orderDtos);

        assertEquals(orderService.getAllOrdersDtoByUserId(1L), orderDtos);

        verify(orderRepository, times(1)).saveAndFlush(orderTest);
        verify(orderRepository, times(2)).findOrderByUserId(1L);
    }


    @Test
    void testGetAllOrdersDtoByUserId() {

        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(1L, Collections.emptyList(), userTest, OrderStatus.CART);

        when(orderRepository.findOrderByUserId(1L)).thenReturn(List.of(orderTest));

        List<OrderDto> orderDtos = new ArrayList<>();
        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        orderDtos.add(new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0));

        when(orderMapper.toOrderDtoList(List.of(orderTest))).thenReturn(orderDtos);

        assertEquals(orderService.getAllOrdersDtoByUserId(1L), orderDtos);
        verify(userServiceImpl, times(0)).getUserById(any());
        verify(orderRepository, times(0)).saveAndFlush(orderTest);
        verify(orderRepository, times(2)).findOrderByUserId(1L);
    }

    @Test
    void testGetCartOrderDtoByUserId() {

        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        Order orderTest = new Order(1L, Collections.emptyList(), userTest, OrderStatus.CART);

        when(orderRepository.findOrderByUserId(1L)).thenReturn(List.of(orderTest));
        when(orderRepository.findOrderByUserIdAndStatus(1L, OrderStatus.CART)).thenReturn(Optional.of(orderTest));

        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0);

        when(orderMapper.toOrderDto(orderTest)).thenReturn(orderDto);

        assertEquals(orderService.getCartOrderDtoByUserId(1L), orderDto);
        assertEquals(orderService.getCartOrderDtoByUserId(1L).getUser().getFirstName(), orderDto.getUser().getFirstName());

        verify(userServiceImpl, times(0)).getUserById(any());
        verify(orderRepository, times(0)).saveAndFlush(any());

    }

    @Test
    void testGetCartOrderDtoByUserId_OrderList_Empty() {

        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(null, null, userTest, OrderStatus.CART);

        OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0);

        when(orderRepository.findOrderByUserId(userTest.getId())).thenReturn(Collections.emptyList());
        when(orderMapper.toOrderDto(orderTest)).thenReturn(orderDto);
        when(orderRepository.saveAndFlush(orderTest)).thenReturn(orderTest);
        when(userServiceImpl.getUserById(userTest.getId())).thenReturn(userTest);
        when(orderService.createEmptyCart(userTest.getId())).thenReturn(orderTest);

        assertEquals(orderService.getCartOrderDtoByUserId(userTest.getId()), orderDto);
        verify(orderRepository, times(1)).saveAndFlush(orderTest);
    }

    @Test
    void testGetCartOrderDtoByUserId_OrderListNotContainsCartStatus() {

        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(null, null, userTest, OrderStatus.CART);

        OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0);


        when(orderRepository.findOrderByUserId(userTest.getId())).thenReturn(List.of(orderTest));
        when(orderRepository.findOrderByUserIdAndStatus(userTest.getId(), OrderStatus.CART)).thenReturn(Optional.empty());

        when(orderService.createEmptyCart(userTest.getId())).thenReturn(orderTest);
        when(orderMapper.toOrderDto(orderTest)).thenReturn(orderDto);

        assertEquals(orderService.getCartOrderDtoByUserId(userTest.getId()), orderDto);
        verify(orderRepository, times(1)).findOrderByUserId(userTest.getId());
        verify(orderRepository, times(1)).saveAndFlush(any());

    }

    @Test
    void testGetOrderById() {
        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(null, null, userTest, OrderStatus.CART);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderTest));

        assertEquals(orderService.getOrderById(1L), orderTest);
    }

    @Test
    void testGetOrderById_AppException() {

        when(orderRepository.findById(1L)).thenThrow(new AppException("Not Found Order", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> {
            orderRepository.findById(1L);
        });

        assertEquals("Not Found Order", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testGetOrderDtoById() {
        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(null, null, userTest, OrderStatus.CART);

        OrderDto orderDto = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CART, 0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderTest));
        when(orderMapper.toOrderDto(orderTest)).thenReturn(orderDto);

        assertEquals(orderService.getOrderDtoById(1L), orderDto);
    }

    @Test
    void testGetOrderDtoById_AppException() {

        when(orderRepository.findById(1L)).thenThrow(new AppException("Not Found Order", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> {
            orderRepository.findById(1L);
        });

        assertEquals("Not Found Order", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testUpdateOrder() {
        User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");
        UserDto user = new UserDto(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "TEST_AUTHORITY");

        Order orderTest = new Order(1L, Collections.emptyList(), userTest, OrderStatus.CART);
        Order orderUpdated = new Order(1L, Collections.emptyList(), userTest, OrderStatus.CREATED);
        OrderDto orderDtoForUpdate = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CREATED, 0);
        OrderDto orderDtoUpdated = new OrderDto(1L, Collections.emptyList(), user, OrderStatus.CREATED, 0);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderTest));
        when(orderMapper.toOrder(orderDtoForUpdate)).thenReturn(orderUpdated);
        when(orderRepository.save(orderUpdated)).thenReturn(orderUpdated);
        when(orderMapper.toOrderDto(any())).thenReturn(orderDtoUpdated);

        assertEquals(orderService.updateOrder(1L, orderDtoForUpdate), orderDtoUpdated);
        verify(orderRepository, times(1)).save(any());
    }
}
