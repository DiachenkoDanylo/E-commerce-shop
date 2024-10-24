package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.ItemServiceImpl;
import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final OrderItemServiceImpl orderItemServiceImpl;

    @GetMapping("/")
    public ResponseEntity<List<ItemDto>> allItems() {
        return ResponseEntity.ok(itemServiceImpl.allItems());
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItemDto = itemServiceImpl.createItem(itemDto);
        return ResponseEntity.created(URI.create("/items/" + createdItemDto.getId())).body(createdItemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemServiceImpl.getItemDto(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> deleteItem(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemServiceImpl.deleteItem(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(itemServiceImpl.updateItem(id, itemDto));
    }

    @GetMapping("/{id}/add")
    public ResponseEntity<OrderItem> addToCartItem(@PathVariable("id") Long id, Authentication auth,
                                                   @RequestParam(name = "quantity", defaultValue = "1") String quantity) {
        User user = userServiceImpl.getUserByLoginAuth(auth);
        return ResponseEntity.ok(orderItemServiceImpl.addToCurrentCartItem(user.getId(), id, Integer.parseInt(quantity)));

    }
}