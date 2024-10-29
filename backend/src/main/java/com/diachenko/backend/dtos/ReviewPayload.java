package com.diachenko.backend.dtos;
/*  E-commerce-shop
    28.10.2024
    @author DiachenkoDanylo
*/

public record ReviewPayload(Long itemId, Integer rating, String comment,
                            Long orderId) {
}
