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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository repository;
    private final WishListMapper mapper;

    @Override
    public Page<WishList> getWishListListByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<WishListDto> getWishListDtoListByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishList> wishLists = repository.findAllByUserId(userId, pageable);
        if (wishLists.getContent().isEmpty()) {
            throw new AppException("User dont have anything in wishlist", HttpStatus.NOT_FOUND);
        } else {
            return new PageImpl<>(mapper.toWishListDtoList(wishLists.getContent()), pageable, wishLists.getTotalPages());
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
