package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.core.services.CategoryServiceImpl;
import com.diachenko.backend.core.services.WishListServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.dtos.WishListDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WishListMapper.class})
public interface WishListMapper {

    WishListDto toWishListDto(WishList wishList);

    WishList toWishList(WishListDto wishListDto);

    List<WishListDto> toWishListDtoList(List<WishList> wishList);

    List<WishList> toWishListList(List<WishListDto> wishListDto);

}
