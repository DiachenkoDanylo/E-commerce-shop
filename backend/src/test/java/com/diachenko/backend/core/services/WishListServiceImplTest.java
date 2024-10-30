package com.diachenko.backend.core.services;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.dtos.WishListDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.WishListMapper;
import com.diachenko.backend.infrastructure.repositories.WishListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishListServiceImplTest {

    Long userId = 1L;
    Long itemId = 2L;
    WishList wishList = new WishList(1L, 1L, 2L, LocalDateTime.now());
    WishListDto wishListDto = new WishListDto(1L, 2L);
    WishList wishList2 = new WishList(2L, 1L, 3L, LocalDateTime.now());
    WishListDto wishListDto2 = new WishListDto(1L, 3L);
    @InjectMocks
    private WishListServiceImpl wishListService;
    @Mock
    private WishListRepository repository;
    @Mock
    private WishListMapper mapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWishListListByUserId() {
        when(repository.findAllByUserId(wishList.getUserId())).thenReturn(List.of(wishList, wishList2));

        assertEquals(wishListService.getWishListListByUserId(userId), List.of(wishList, wishList2));
        verify(repository, times(1)).findAllByUserId(any());
    }

    @Test
    void testGetWishListDtoListByUserId() {
        when(repository.findAllByUserId(wishList.getUserId())).thenReturn(List.of(wishList, wishList2));
        when(mapper.toWishListDtoList(List.of(wishList, wishList2))).thenReturn(List.of(wishListDto, wishListDto2));

        assertEquals(wishListService.getWishListDtoListByUserId(userId), List.of(wishListDto, wishListDto2));
        verify(repository, times(1)).findAllByUserId(any());
        verify(mapper, times(1)).toWishListDtoList(any());
    }

    @Test
    void testGetWishListDtoListByUserId_AppException() {
        when(repository.findAllByUserId(wishList.getUserId())).thenReturn(Collections.emptyList());

        AppException thrown = assertThrows(AppException.class, () -> {
            wishListService.getWishListDtoListByUserId(userId);
        });

        assertEquals("User dont have anything in wishlist", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(repository, times(1)).findAllByUserId(any());
    }

    @Test
    void testAddItemToWishList() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(wishList);
        when(mapper.toWishListDto(any())).thenReturn(wishListDto);

        assertEquals(wishListService.addItemToWishList(userId, itemId), wishListDto);
        verify(repository, times(1)).findByUserIdAndItemId(userId, itemId);
        verify(mapper, times(1)).toWishListDto(any());
    }

    @Test
    void testAddItemToWishList_AppException() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.of(wishList));

        AppException thrown = assertThrows(AppException.class, () -> {
            wishListService.addItemToWishList(userId, itemId);
        });

        assertEquals("Item is already in the wishlist", thrown.getMessage());
        assertEquals(thrown.getStatus(), HttpStatus.BAD_REQUEST);
        verify(repository, times(1)).findByUserIdAndItemId(userId, itemId);
    }

    @Test
    void testRemoveItemFromWishList() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.of(wishList));

        assertDoesNotThrow(() -> wishListService.removeItemFromWishList(userId, itemId));

        verify(repository, times(1)).findByUserIdAndItemId(userId, itemId);
        verify(repository, times(1)).deleteByUserIdAndItemId(userId, itemId);
    }

    @Test
    void testRemoveItemFromWishList_AppException() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            wishListService.removeItemFromWishList(userId, itemId);
        });

        assertEquals("Item not found in the wishlist", thrown.getMessage());
        assertEquals(thrown.getStatus(), HttpStatus.BAD_REQUEST);
        verify(repository, times(1)).findByUserIdAndItemId(userId, itemId);
    }

    @Test
    void testGetWishListDto() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.of(wishList));
        when(mapper.toWishListDto(wishList)).thenReturn(wishListDto);

        assertEquals(wishListService.getWishListDto(userId, itemId), wishListDto);
    }

    @Test
    void testGetWishListDto_AppException() {
        when(repository.findByUserIdAndItemId(userId, itemId)).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            wishListService.getWishListDto(userId, itemId);
        });

        assertEquals("Item in wishlist not found", thrown.getMessage());
        assertEquals(thrown.getStatus(), HttpStatus.NOT_FOUND);
        verify(repository, times(1)).findByUserIdAndItemId(userId, itemId);
    }

    @Test
    void testFindWishListById() {
        when(repository.findById(itemId)).thenReturn(Optional.of(wishList));

        assertEquals(wishListService.findWishListById(itemId), wishList);
    }

    @Test
    void testFindWishListById_AppException() {
        when(repository.findById(itemId)).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            wishListService.findWishListById(itemId);
        });

        assertEquals("Item in wishlist not found", thrown.getMessage());
        assertEquals(thrown.getStatus(), HttpStatus.NOT_FOUND);
        verify(repository, times(1)).findById(itemId);
    }

}