package com.diachenko.backend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class OrderItemDto {

    private Long id;
    @NotNull
    private ItemDto item;
    private int quantity;
    @NotNull
    private Integer totalPrice;
}