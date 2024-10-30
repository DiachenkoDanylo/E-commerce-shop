package com.diachenko.backend.application.services;

import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.entities.SearchCriteria;
import com.diachenko.backend.dtos.ItemDto;

import java.util.List;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

public interface ItemService {


    List<ItemDto> getAllItems();

    ItemDto getItemDto(Long id);

    ItemDto createItem(ItemDto itemDto);

    ItemDto deleteItem(Long id);

    ItemDto updateItem(Long id, ItemDto itemDto);

    Item findItemById(Long itemId);

    List<ItemDto> searchItems(SearchCriteria criteria);
}
