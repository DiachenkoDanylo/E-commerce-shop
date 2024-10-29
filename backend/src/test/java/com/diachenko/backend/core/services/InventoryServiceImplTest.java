package com.diachenko.backend.core.services;
/*  E-commerce-shop
    16.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.repositories.CategoryRepository;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class InventoryServiceImplTest {

    ItemDto itemDto = new ItemDto(1L, "testItem1",1L, "testDesc1", 100, 10);

    @Mock
    private ItemServiceImpl itemServiceImpl;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsAvailible_true() {
        when(itemServiceImpl.getItemDto(1L)).thenReturn(itemDto);

        assertTrue(inventoryService.isAvailible(1L, 8));
    }

    @Test
    void testIsAvailible_false() {
        when(itemServiceImpl.getItemDto(1L)).thenReturn(itemDto);

        assertFalse(inventoryService.isAvailible(1L, 11));
    }

    @Test
    void testChangeItemQuantity() {
        Category category = new Category(1L, "testing category name","testing category description");
        Item item2 = new Item(1L, "testItem1",category, "testDesc1", 100, 8);
        ItemDto itemDto2 = new ItemDto(1L, "testItem1",category.getId(), "testDesc1", 100, 8);

        when(itemServiceImpl.getItemDto(1L)).thenReturn(itemDto);
        when(itemMapper.toItemDto(item2)).thenReturn(itemDto2);
        when(itemMapper.toItem(itemDto2,categoryService)).thenReturn(item2);
        when(itemRepository.save(item2)).thenReturn(item2);

        assertEquals(inventoryService.changeItemQuantity(1L, 2), itemDto2);
    }
}
