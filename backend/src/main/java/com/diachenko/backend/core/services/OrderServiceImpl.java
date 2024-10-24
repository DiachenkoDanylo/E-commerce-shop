package com.diachenko.backend.core.services;
/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.OrderService;
import com.diachenko.backend.core.entities.OrderStatus;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.OrderMapper;
import com.diachenko.backend.infrastructure.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceImpl userServiceImpl;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderMapper.toOrderDtoList(orderRepository.findAll());
    }

    @Override
    public List<OrderDto> getAllOrdersWithStatus(OrderStatus orderStatus) {
        return orderMapper.toOrderDtoList(orderRepository.findAllByStatus(orderStatus));
    }

    @Override
    public List<OrderDto> getAllOrdersDtoByUserId(Long userId) {
        if (orderRepository.findOrderByUserId(userId).isEmpty()) {
            System.out.println("User with id:" + userId + " didnt have any orders before \n Creating order...");
            User user = userServiceImpl.getUserById(userId);
            Order order = new Order(user, OrderStatus.CART);
            orderRepository.saveAndFlush(order);
        }
        return orderMapper.toOrderDtoList(orderRepository.findOrderByUserId(userId));
    }

    @Override
    public Order createEmptyCart(Long userId) {
        User user = userServiceImpl.getUserById(userId);
        Order order = new Order(user, OrderStatus.CART);
        return orderRepository.saveAndFlush(order);
    }

    @Override
    public OrderDto getCartOrderDtoByUserId(Long userId) {
        if (orderRepository.findOrderByUserId(userId).isEmpty()) {
            return orderMapper.toOrderDto(createEmptyCart(userId));
        }
        Optional<Order> optional = orderRepository.findOrderByUserIdAndStatus(userId, OrderStatus.CART);
        if (optional.isPresent()) {
            return orderMapper.toOrderDto(optional.get());
        } else {
            return orderMapper.toOrderDto(createEmptyCart(userId));
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new AppException("Not Found Order", HttpStatus.NOT_FOUND));
    }

    @Override
    public OrderDto getOrderDtoById(Long orderId) {
        return orderMapper.toOrderDto(orderRepository.findById(orderId).orElseThrow(() -> new AppException("Not Found Order", HttpStatus.NOT_FOUND)));
    }

    @Override
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));
        orderMapper.updateOrder(order, orderMapper.toOrder(orderDto));

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderDto(updatedOrder);
    }


}
