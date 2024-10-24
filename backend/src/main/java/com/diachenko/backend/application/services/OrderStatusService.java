package com.diachenko.backend.application.services;
/*  E-commerce-shop
    13.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.dtos.OrderDto;

public interface OrderStatusService {

    OrderDto checkoutOrderByUserId(Long userId);

    OrderDto changeStatusOrderByOrderId(Long orderId, String status);
}
