package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    11.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.dtos.OrderItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest {

    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE);
    private static final String BASE_URI = "/order-item/";
    ItemDto item1Dto = new ItemDto(2L, "testItem1", 1L, "testDesc1", 100, null, 10);
    OrderItemDto orderItemDto = new OrderItemDto(1L, item1Dto, 1, 100);
    @MockBean
    private OrderItemServiceImpl orderItemService;

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testGetCartOrderItem() throws Exception {
        Page<OrderItemDto> result = new PageImpl<>(List.of(orderItemDto), PAGEABLE, 1);
        when(orderItemService.getOrderItemList(1L, PAGE, SIZE)).thenReturn(result);

        mockMvc.perform(get(BASE_URI + "{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].totalPrice").value(100));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testUpdateCartOrderItem() throws Exception {
        Long id = 1L;

        OrderItemDto updatedOrderItemDto = new OrderItemDto(1L, item1Dto, 2, 200);

        when(orderItemService.update(id, List.of(updatedOrderItemDto))).thenReturn(List.of(updatedOrderItemDto));

        mockMvc.perform(put(BASE_URI + "{id}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(List.of(updatedOrderItemDto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].totalPrice").value(200));
    }

}
