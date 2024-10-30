package com.diachenko.backend.core.services;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.WishListService;
import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.dtos.WishListDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.WishListMapper;
import com.diachenko.backend.infrastructure.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository repository;
    private final WishListMapper mapper;

    @Override
    public List<WishList> getWishListListByUserId(Long userId) {
        return repository.findAllByUserId(userId);
    }

    @Override
    public List<WishListDto> getWishListDtoListByUserId(Long userId) {
        List<WishList> wishLists = repository.findAllByUserId(userId);
        if (wishLists.isEmpty()) {
            throw new AppException("User dont have anything in wishlist", HttpStatus.NOT_FOUND);
        } else {
            return mapper.toWishListDtoList(wishLists);
        }
    }

    @Override
    public WishListDto addItemToWishList(Long userId, Long itemId) {
        // Check if the item is already on the user's wishlist
        Optional<WishList> existingWishListItem = repository.findByUserIdAndItemId(userId, itemId);

        if (existingWishListItem.isPresent()) {
            throw new AppException("Item is already in the wishlist", HttpStatus.BAD_REQUEST);
        }

        WishList wishlist = new WishList();
        wishlist.setUserId(userId);
        wishlist.setItemId(itemId);

        WishList wishList = repository.save(wishlist);
        return mapper.toWishListDto(wishList);
    }

    @Transactional
    @Override
    public void removeItemFromWishList(Long userId, Long itemId) {
        Optional<WishList> wishlistItem = repository.findByUserIdAndItemId(userId, itemId);

        if (wishlistItem.isEmpty()) {
            throw new AppException("Item not found in the wishlist", HttpStatus.BAD_REQUEST);
        }

        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public WishListDto getWishListDto(Long userId, Long itemId) {
        return mapper.toWishListDto(repository.findByUserIdAndItemId(userId, itemId).orElseThrow(
                () -> new AppException("Item in wishlist not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public WishList findWishListById(Long itemId) {
        return repository.findById(itemId).orElseThrow(
                () -> new AppException("Item in wishlist not found", HttpStatus.NOT_FOUND));
    }
}
