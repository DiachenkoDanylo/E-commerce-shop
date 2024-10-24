package com.diachenko.backend.dtos;
/*  E-commerce-shop
    10.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderDto {

    private Long id;

    private List<OrderItemDto> items;

    @NotNull
    private UserDto user;

    @NotNull
    private OrderStatus status;

    private Integer orderSum;

}
