package com.diachenko.backend.infrastructure.mappers;

import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.dtos.OrderItemDto;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.core.entities.Order;
import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.core.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/
@Mapper(componentModel = "spring", uses = {Order.class})
public interface OrderMapper {

    Order toOrder(OrderDto orderDto);

    @Mapping(target = "items", ignore = true)
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtoList(List<Order> orderList);

    void updateOrder(@MappingTarget Order target, Order source);

    default User toUser(UserDto userDto) {
        return Mappers.getMapper(UserMapper.class).toUser(userDto);
    }

    default UserDto toUserDto(User user) {
        return Mappers.getMapper(UserMapper.class).toUserDto(user);
    }

    default List<OrderItem> toOrderItemList(List<OrderItemDto> orderItemDtoList) {
        return Mappers.getMapper(OrderItemMapper.class).toOrderItemList(orderItemDtoList);
    }

    default List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItemList) {
        return Mappers.getMapper(OrderItemMapper.class).toOrderItemDtoList(orderItemList);
    }


}