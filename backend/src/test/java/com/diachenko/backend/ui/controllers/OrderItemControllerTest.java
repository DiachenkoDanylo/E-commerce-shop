package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.OrderServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest {

    User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "ADMIN");

    @MockBean
    private OrderServiceImpl orderServiceImpl;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testGetCartOrderItem() {

        when(userServiceImpl.getUserByLoginAuth(any())).thenReturn(userTest);

        assertEquals(userTest.getId(), 1L);
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
