package com.diachenko.backend.core.services;
/*  E-commerce-shop
    16.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ItemMapper;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    public List<Item> mockItemList() {
        Category category = new Category(1L, "testing category", "testing description");
        Item item1 = new Item(1L, "testItem1", category, "testDesc1", 100, null, 10);
        Item item2 = new Item(2L, "testItem2", category, "testDesc2", 200, null, 20);
        return List.of(item1, item2);
    }

    public List<ItemDto> mockItemDtoList() {
        ItemDto item1 = new ItemDto(1L, "testItem1", 1L, "testDesc1", 100, Collections.emptyList(), 10);
        ItemDto item2 = new ItemDto(2L, "testItem2", 1L, "testDesc2", 200,Collections.emptyList(), 20);
        return List.of(item1, item2);
    }

    @Test
    void testGetAllItems() {
        List<Item> itemsList = mockItemList();
        List<ItemDto> itemDtoList = mockItemDtoList();


        when(itemRepository.findAll()).thenReturn(itemsList);
        when(itemMapper.toItemDtos(itemsList)).thenReturn(itemDtoList);

        assertEquals(itemService.getAllItems(), itemDtoList);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void testGetItemDto() {
        List<Item> itemsList = mockItemList();
        List<ItemDto> itemDtoList = mockItemDtoList();

        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(itemsList.get(0)));
        when(itemMapper.toItemDto(itemsList.get(0))).thenReturn(itemDtoList.get(0));

        assertEquals(itemService.getItemDto(1L), itemDtoList.get(0));
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testGetItemDto_AppException() {

        when(itemRepository.findById(1L)).thenThrow(new AppException("item not found", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> {
            itemService.getItemDto(1L);
        });

        assertEquals("item not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateItem() {

        List<Item> itemsList = mockItemList();
        List<ItemDto> itemDtoList = mockItemDtoList();

        when(itemMapper.toItem(itemDtoList.get(0), categoryService)).thenReturn(itemsList.get(0));
        when(itemRepository.save(itemsList.get(0))).thenReturn(itemsList.get(0));
        when(itemMapper.toItemDto(itemsList.get(0))).thenReturn(itemDtoList.get(0));

        assertEquals(itemService.createItem(itemDtoList.get(0)), itemDtoList.get(0));
        verify(itemRepository, times(1)).save(itemsList.get(0));
    }

    @Test
    void testDeleteItem() {
        List<Item> itemsList = mockItemList();
        List<ItemDto> itemDtoList = mockItemDtoList();

        when(itemRepository.findById(2L)).thenReturn(Optional.of(itemsList.get(1)));

        when(itemMapper.toItemDto(itemsList.get(1))).thenReturn(itemDtoList.get(1));

        assertEquals(itemService.deleteItem(2L), itemDtoList.get(1));
        verify(itemRepository, times(1)).deleteById(2L);
    }

    @Test
    void testDeleteItem_AppException() {

        when(itemRepository.findById(2L)).thenThrow(new AppException("item not found", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> {
            itemService.deleteItem(2L); // Вызов метода, который должен выбросить исключение
        });

        assertEquals("item not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        verify(itemRepository, times(1)).findById(2L);
    }

//    ItemDto updateItem(Long id, ItemDto itemDto);
}
