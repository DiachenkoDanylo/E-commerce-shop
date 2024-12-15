package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    04.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.OrderServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.OrderDto;
import com.diachenko.backend.exceptions.AppException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/")
@RequiredArgsConstructor
public class OrderController {

    private final UserServiceImpl userServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    @GetMapping
    public ResponseEntity<OrderDto> getCurrentOrder(Authentication auth) {
        User user = userServiceImpl.getUserByLoginAuth(auth);
        return ResponseEntity.ok(orderServiceImpl.getCartOrderDtoByUserId(user.getId()));
    }

    @GetMapping("all")
    public ResponseEntity<Page<OrderDto>> getAllOrders(Authentication auth,
           @RequestParam(name = "page", defaultValue = "0") int page,
           @RequestParam(name = "size", defaultValue = "10") int size) {
        User user = userServiceImpl.getUserByLoginAuth(auth);
        return ResponseEntity.ok(orderServiceImpl.getAllOrdersDtoByUserId(user.getId(),page,size));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderDto> getOrderClient(Authentication auth, @PathVariable("id") Long id) {
        if (orderServiceImpl.getOrderDtoById(id).getUser().getLogin().equals(userServiceImpl.getUserByLoginAuth(auth).getLogin()) || auth.getPrincipal().toString().contains("ADMIN")) {
            return ResponseEntity.ok(orderServiceImpl.getOrderDtoById(id));
        } else {
            throw new AppException("You cannot get others client orders", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("id") Long id, @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderServiceImpl.updateOrder(id, orderDto));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Page<OrderDto>> getAllOrders(@PathVariable("userId") Long userId,
                                                       @RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(orderServiceImpl.getAllOrdersDtoByUserId(userId,page,size));
    }
}
