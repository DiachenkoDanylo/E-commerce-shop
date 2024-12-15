package com.diachenko.backend.core.services;

import com.diachenko.backend.application.services.ItemService;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.entities.ItemSpecifications;
import com.diachenko.backend.core.entities.SearchCriteria;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final String ERROR_NOT_FOUND = "item not found";
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final CategoryServiceImpl categoryService;

    @Override
    @Cacheable(value = "allItemsPage")
    public Page<ItemDto> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findAll(pageable);
        return new PageImpl<>(itemMapper.toItemDtos(itemPage.getContent()), pageable, itemPage.getTotalPages());
    }

    @Override
    @Cacheable(value = "ItemDto",  key = "#p0")
    public ItemDto getItemDto(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException(ERROR_NOT_FOUND, HttpStatus.NOT_FOUND));
        return itemMapper.toItemDto(item);
    }

    @Override
    @Caching(cacheable = {
            @Cacheable(value = "ItemDto", key = "#p0.id")
    })
    public ItemDto createItem(ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto, categoryService);

        Item createdItem = itemRepository.save(item);

        return itemMapper.toItemDto(createdItem);
    }

    @Override
    @CacheEvict(value = "ItemDto", key = "#p0")
    public ItemDto deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException(ERROR_NOT_FOUND, HttpStatus.NOT_FOUND));
        itemRepository.deleteById(id);
        return itemMapper.toItemDto(item);
    }

    @Override
    @Caching(put = {
        @CachePut(value = "ItemDto", key = "#p0")
    })
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AppException(ERROR_NOT_FOUND, HttpStatus.NOT_FOUND));
        itemMapper.updateItem(item, itemMapper.toItem(itemDto, categoryService));

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toItemDto(updatedItem);
    }

    @Override
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new AppException(ERROR_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    @Cacheable(value = "ItemService::searchItems")
    public Page<ItemDto> searchItems(SearchCriteria criteria, int page, int size) {
        Specification<Item> spec = Specification.where(ItemSpecifications.hasKeyword(criteria.getKeyword()))
                .and(ItemSpecifications.hasCategory(criteria.getCategoryId()))
                .and(ItemSpecifications.hasPriceBetween(criteria.getMinPrice(), criteria.getMaxPrice()))
                .and(ItemSpecifications.isInStock(criteria.getInStock()));
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> productPage = itemRepository.findAll(spec, pageable);
        return new PageImpl<>(itemMapper.toItemDtos(productPage.getContent()), pageable, productPage.getTotalPages());
    }
}
