package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.OrderServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.OrderItemDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest {

    @MockBean
    private OrderServiceImpl orderServiceImpl;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "ADMIN");

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testGetCartOrderItem() {

        when(userServiceImpl.getUserByLoginAuth(any())).thenReturn(userTest);

        assertEquals(userTest.getId(),1L);
        // developing...
    }
//
//    @GetMapping("/order/cart")
//    public ResponseEntity<List<OrderItemDto>> getCartOrderItem(Authentication auth) {
//        User user = userServiceImpl.getUserByLoginAuth(auth);
//        orderServiceImpl.getCartOrderDtoByUserId(user.getId());
//        return ResponseEntity.ok(null);
//    }

}
