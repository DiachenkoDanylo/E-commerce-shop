package com.diachenko.backend.application.services;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.dtos.WishListDto;
import org.springframework.data.domain.Page;

public interface WishListService {

    Page<WishList> getWishListListByUserId(Long userId, int page, int size);

    Page<WishListDto> getWishListDtoListByUserId(Long userId, int page, int size);

    WishListDto addItemToWishList(Long userId, Long itemId);

    void removeItemFromWishList(Long userId, Long itemId);

    WishListDto getWishListDto(Long userId, Long itemId);

    WishList findWishListById(Long itemId);
}
