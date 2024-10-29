package com.diachenko.backend.core.services;

import com.diachenko.backend.application.services.ItemService;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDto> allItems() {
        return itemMapper.toItemDtos(itemRepository.findAll());
    }

    @Override
    public ItemDto getItemDto(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException("item not found", HttpStatus.NOT_FOUND));
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);

        Item createdItem = itemRepository.save(item);

        return itemMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException("item not found", HttpStatus.NOT_FOUND));
        itemRepository.deleteById(id);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException("item not found", HttpStatus.NOT_FOUND));
        itemMapper.updateItem(item, itemMapper.toItem(itemDto));

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public Item findItemById(Long itemId) {
        return  itemRepository.findById(itemId).orElseThrow(() -> new AppException("item not found", HttpStatus.NOT_FOUND));
    }
}
