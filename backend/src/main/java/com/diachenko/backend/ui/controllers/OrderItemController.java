package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.dtos.OrderItemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-item/")
public class OrderItemController {

    private final OrderItemServiceImpl orderItemServiceImpl;

    @GetMapping("{id}")
    public ResponseEntity<Page<OrderItemDto>> getCartOrderItem(@PathVariable("id") Long id,
                                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(orderItemServiceImpl.getOrderItemList(id, page, size));
    }

    @PutMapping("{id}")
    public ResponseEntity<List<OrderItemDto>> updateCartOrderItem(@Valid @RequestBody List<OrderItemDto> orderItemDtos,
                                                                  @PathVariable("id") Long id) {
        return ResponseEntity.ok(orderItemServiceImpl.update(id, orderItemDtos));
    }


}
