package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.dtos.WishListDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {WishList.class})
public interface WishListMapper {

    WishListDto toWishListDto(WishList wishList);

    WishList toWishList(WishListDto wishListDto);

    List<WishListDto> toWishListDtoList(List<WishList> wishList);

    List<WishList> toWishListList(List<WishListDto> wishListDto);

}
