package com.diachenko.backend.core.services;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.InventoryService;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ItemServiceImpl itemServiceImpl;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CategoryServiceImpl categoryService;

    @Override
    public boolean isAvailible(Long itemId, Integer quantity) {
        return (itemServiceImpl.getItemDto(itemId).getQuantity() > quantity);
    }

    @Override
    public ItemDto changeItemQuantity(Long itemId, Integer quantity) {
        ItemDto item = itemServiceImpl.getItemDto(itemId);
        item.setQuantity(item.getQuantity() - quantity);
        return itemMapper.toItemDto(itemRepository.save(itemMapper.toItem(item, categoryService)));
    }
}
