package com.diachenko.backend.application.services;

import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.entities.SearchCriteria;
import com.diachenko.backend.dtos.ItemDto;
import org.springframework.data.domain.Page;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

public interface ItemService {


    Page<ItemDto> getAllItems(int page, int size);

    ItemDto getItemDto(Long id);

    ItemDto createItem(ItemDto itemDto);

    ItemDto deleteItem(Long id);

    ItemDto updateItem(Long id, ItemDto itemDto);

    Item findItemById(Long itemId);

    Page<ItemDto> searchItems(SearchCriteria criteria, int page, int size);
}
