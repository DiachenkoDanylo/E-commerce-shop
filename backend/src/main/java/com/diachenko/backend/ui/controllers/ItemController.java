package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.core.entities.SearchCriteria;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.ItemServiceImpl;
import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final OrderItemServiceImpl orderItemService;

    @GetMapping("/")
    public ResponseEntity<Page<ItemDto>> allItems(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(itemService.getAllItems(page, size));
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItemDto = itemService.createItem(itemDto);
        return ResponseEntity.created(URI.create("/items/" + createdItemDto.getId())).body(createdItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.getItemDto(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> deleteItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.deleteItem(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDto));
    }

    @GetMapping("/{id}/add")
    public ResponseEntity<OrderItem> addToCartItem(@PathVariable("id") Long id, Authentication auth,
                                                   @RequestParam(name = "quantity", defaultValue = "1") String quantity) {
        User user = userService.getUserByLoginAuth(auth);
        return ResponseEntity.ok(orderItemService.addToCurrentCartItem(user.getId(), id, Integer.parseInt(quantity)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemDto>> searchItems(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "inStock", required = false) Boolean inStock,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(itemService
                .searchItems(new SearchCriteria(keyword, categoryId, minPrice, maxPrice, inStock), page, size));
    }
}