package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.core.services.ItemServiceImpl;
import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemServiceImpl itemServiceImpl;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private OrderItemServiceImpl orderItemServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    ItemDto itemDto1 = new ItemDto(1L, "testItem1",1L, "testDesc1", 100, 10);
    ItemDto itemDto2 = new ItemDto(2L, "testItem2",1L, "testDesc2", 200, 20);

    ItemDto itemDtoForCreation = new ItemDto(null, "testItem2",2L, "testDesc2", 200, 20);
    ItemDto createdItemDto = new ItemDto(2L, "testItem2",2L, "testDesc2", 200, 20);

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testAllItems() throws Exception {
        when(itemServiceImpl.getAllItems()).thenReturn(List.of(itemDto1, itemDto2));

        mockMvc.perform(get("/items/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(20));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testCreateItem() throws Exception {

        when(itemServiceImpl.createItem(any(ItemDto.class))).thenReturn(createdItemDto);

        mockMvc.perform(post("/items/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemDtoForCreation))
                        .with(csrf()))      // mockMVC is  always setting up the csrf, need to manually disable
                                            // it in testSecurityConfig or use csrf token in tests
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("testItem2"))
                .andExpect(header().string("Location", "/items/2"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    @PostMapping("/items")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
//        ItemDto createdItemDto = itemServiceImpl.createItem(itemDto);
//        return ResponseEntity.created(URI.create("/items/" + createdItemDto.getId())).body(createdItemDto);
//    }
//
//    @GetMapping("/items/{id}")
//    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(itemServiceImpl.getItemDto(id));
//    }
//
//
//    @DeleteMapping("/items/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<ItemDto> deleteItem(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(itemServiceImpl.deleteItem(id));
//    }
//
//    @PutMapping("/items/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ItemDto itemDto) {
//        return ResponseEntity.ok(itemServiceImpl.updateItem(id, itemDto));
//    }
//
//    @GetMapping("/items/{id}/add")
//    public ResponseEntity<OrderItem> addToCartItem(@PathVariable("id") Long id, Authentication auth,
//                                                   @RequestParam(name = "quantity", defaultValue = "1") String quantity) {
//        User user = userServiceImpl.getUserByLoginAuth(auth);
//        return ResponseEntity.ok(orderItemServiceImpl.addToCurrentCartItem(user.getId(), id, Integer.parseInt(quantity)));
//
//    }
}