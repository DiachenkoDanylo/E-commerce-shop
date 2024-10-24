package com.diachenko.backend.application.services;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.dtos.ItemDto;


public interface InventoryService {

    boolean isAvailible(Long itemId, Integer quantity);

    ItemDto changeItemQuantity(Long itemId, Integer quantity);
}
