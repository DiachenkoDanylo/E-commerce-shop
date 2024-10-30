package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.core.services.WishListServiceImpl;
import com.diachenko.backend.dtos.WishListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist/")
@RequiredArgsConstructor
public class WishListController {

    private final UserServiceImpl userService;
    private final WishListServiceImpl wishListService;

    @GetMapping("")
    public ResponseEntity<List<WishListDto>> getWishListListByUser(Authentication auth) {
        User user = userService.getUserByLoginAuth(auth);// extract user ID from JWT
        return ResponseEntity.ok(wishListService.getWishListDtoListByUserId(user.getId()));
    }

    @PostMapping("{itemId}")
    public ResponseEntity<WishListDto> addToWishlistByUserAndItemId(@PathVariable("itemId") Long itemId, Authentication auth) {
        User user = userService.getUserByLoginAuth(auth);
        return ResponseEntity.ok(wishListService.addItemToWishList(user.getId(), itemId));
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<String> deleteWishList(@PathVariable("itemId") Long itemId, Authentication auth) {
        User user = userService.getUserByLoginAuth(auth);
        wishListService.removeItemFromWishList(user.getId(), itemId);
        return ResponseEntity.ok("Item removed");
    }
}
