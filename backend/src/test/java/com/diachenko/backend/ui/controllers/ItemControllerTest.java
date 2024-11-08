package com.diachenko.backend.ui.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.diachenko.backend.core.entities.Authority;
import com.diachenko.backend.core.entities.OrderItem;
import com.diachenko.backend.core.entities.SearchCriteria;
import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.services.ItemServiceImpl;
import com.diachenko.backend.core.services.OrderItemServiceImpl;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*  E-commerce shop
    25.09.2024
    @author DiachenkoDanylo
*/
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private final UserDto userDto = new UserDto(1L, "testname", "testlname", "testlog", "testpass", "testmail", "ADMIN");
    private final User user = new User(1L, "testname", "testlname", "testlog", "testpass", "testmail", "ADMIN");
    ItemDto itemDto1 = new ItemDto(1L, "testItem1", 1L, "testDesc1", 100, Collections.emptyList(), 10);
    ItemDto itemDto2 = new ItemDto(2L, "testItem2", 1L, "testDesc2", 200, Collections.emptyList(), 20);
    ItemDto itemDtoForCreation = new ItemDto(null, "testItem2", 2L, "testDesc2", 200, Collections.emptyList(), 20);
    ItemDto createdItemDto = new ItemDto(2L, "testItem2", 2L, "testDesc2", 200, Collections.emptyList(), 20);

    @MockBean
    private ItemServiceImpl itemService;
    @MockBean
    private UserServiceImpl userService;
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
    void testAllItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of(itemDto1, itemDto2));

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

        when(itemService.createItem(any(ItemDto.class))).thenReturn(createdItemDto);

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

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testGetItem() throws Exception {
        Long itemId = 1L;
        when(itemService.getItemDto(itemId)).thenReturn(itemDto1);

        mockMvc.perform(get("/items/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("testDesc1"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testDeleteItem() throws Exception {
        Long itemId = 1L;
        when(itemService.deleteItem(itemId)).thenReturn(itemDto1);

        mockMvc.perform(delete("/items/{id}", itemId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("testDesc1"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testUpdateItem() throws Exception {
        Long itemId = 1L;
        when(itemService.updateItem(itemId, itemDto2)).thenReturn(itemDto1);

        mockMvc.perform(put("/items/{id}", itemId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemDto2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("testDesc1"));
    }

    public Authentication auth() {
        return validateToken(createToken(userDto));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secretKey");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        UserDto user2 = UserDto.builder()
                .login(decodedJWT.getIssuer())
                .firstName(decodedJWT.getClaim("firstName").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .authority(decodedJWT.getClaim("authority").asString())
                .build();
        return new UsernamePasswordAuthenticationToken(user2, null, List.of(new Authority(userDto.getAuthority())));
    }

    public String createToken(UserDto userDto) {
        Date now = new Date();
        Date validaty = new Date(now.getTime() + 3_600_000);
        return JWT.create()
                .withIssuer(userDto.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validaty)
                .withClaim("firstName", userDto.getFirstName())
                .withClaim("lastName", userDto.getLastName())
                .withClaim("authority", userDto.getAuthority())
                .sign(Algorithm.HMAC256("secretKey"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testAddToCartItem() throws Exception {
        Long itemId = 1L;
        when(itemService.updateItem(itemId, itemDto2)).thenReturn(itemDto1);
        User mockUser = user;

        OrderItem mockOrderItem = new OrderItem();
        mockOrderItem.setId(itemId);

        Authentication auth = auth();
        when(userService.getUserByLoginAuth(any(Authentication.class))).thenReturn(mockUser);
        when(orderItemService.addToCurrentCartItem(1L, itemId, 1)).thenReturn(mockOrderItem);

        mockMvc.perform(get("/items/{id}/add", itemId)
                        .param("quantity", String.valueOf(1))
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockOrderItem.getId()));

        verify(userService).getUserByLoginAuth(any());
        verify(orderItemService).addToCurrentCartItem(1L, itemId, 1);
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{USER}")
    void testSearchItems() throws Exception {
        List<ItemDto> mockItems = List.of(itemDto1, itemDto2);
        when(itemService.searchItems(any(SearchCriteria.class))).thenReturn(mockItems);

        mockMvc.perform(get("/items/search")
                        .param("keyword", "laptop")
                        .param("categoryId", "1")
                        .param("minPrice", "1000")
                        .param("maxPrice", "2000")
                        .param("inStock", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mockItems.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value("testItem1"));

        verify(itemService, times(1)).searchItems(any(SearchCriteria.class));
    }
}