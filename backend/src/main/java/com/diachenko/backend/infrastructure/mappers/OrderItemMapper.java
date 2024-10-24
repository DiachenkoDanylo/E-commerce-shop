package com.diachenko.backend.infrastructure.mappers;

import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.OrderItemDto;
import com.diachenko.backend.core.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/
@Mapper(componentModel = "spring", uses = {OrderItem.class})
public interface OrderItemMapper {

    OrderItem toOrderItem(OrderItemDto orderItemDto);

    OrderItemDto toOrderItemDto(OrderItem orderItem);

    List<OrderItem> toOrderItemList(List<OrderItemDto> orderItemDtoList);

    List<OrderItemDto> toOrderItemDtoList(List<OrderItem> orderItemList);

    void updateOrderItemList(@MappingTarget List<OrderItem> target, List<OrderItem> source);
}
