package com.diachenko.backend.application.services;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.dtos.WishListDto;

import java.util.List;

public interface WishListService {

    List<WishList> getWishListListByUserId(Long userId);

    List<WishListDto> getWishListDtoListByUserId(Long userId);

    WishListDto addItemToWishList(Long userId, Long itemId);

    void removeItemFromWishList(Long userId, Long itemId);

    WishListDto getWishListDto(Long userId, Long itemId);

    WishList findWishListById(Long itemId);
}
