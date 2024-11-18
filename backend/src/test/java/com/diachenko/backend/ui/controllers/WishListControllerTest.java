package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.core.entities.User;
import com.diachenko.backend.core.entities.WishList;
import com.diachenko.backend.core.services.UserServiceImpl;
import com.diachenko.backend.core.services.WishListServiceImpl;
import com.diachenko.backend.dtos.UserDto;
import com.diachenko.backend.dtos.WishListDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/
@WebMvcTest(WishListController.class)
class WishListControllerTest {

    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final Pageable PAGEABLE = PageRequest.of(PAGE, SIZE);
    private static final String BASE_URI = "/wishlist/";

    @Autowired
    MockMvc mockMvc;

    UserDto user = new UserDto(1L, "testname", "testlname", "testlog", "testpass", "testmail", "TEST_AUTHORITY");
    User userTest = new User(1L, "testname", "testlastname", "testlogin", "testpass", "testemail", "ADMIN");
    WishList wishList = new WishList(1L, 1L, 2L, LocalDateTime.now());
    WishListDto wishListDto = new WishListDto(1L, 2L);
    WishListDto wishListDto2 = new WishListDto(1L, 3L);
    Long itemId = wishList.getItemId();

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private WishListServiceImpl wishListService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void getWishListListByUser() throws Exception {
        Page<WishListDto> pageDto = new PageImpl<>(List.of(wishListDto, wishListDto2), PAGEABLE, 2);

        when(userService.getUserByLoginAuth(any())).thenReturn(userTest);

        when(wishListService.getWishListDtoListByUserId(userTest.getId(), PAGE, SIZE)).thenReturn(pageDto);

        mockMvc.perform(get(BASE_URI)).andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(1L))
                .andExpect(jsonPath("$.content[0].itemId").value(2L))
                .andExpect(jsonPath("$.content[1].userId").value(1L))
                .andExpect(jsonPath("$.content[1].itemId").value(3L));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testAddToWishlistByUserAndItemId() throws Exception {
        when(userService.getUserByLoginAuth(any())).thenReturn(userTest);

        when(wishListService.addItemToWishList(user.getId(), itemId)).thenReturn(wishListDto);

        mockMvc.perform(post(BASE_URI+"{itemId}", itemId).contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishListDto)).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemId").value(2L));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testDeleteWishList() throws Exception {
        when(userService.getUserByLoginAuth(any())).thenReturn(userTest);

        mockMvc.perform(delete(BASE_URI+"{itemId}", itemId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Item removed"));

        verify(wishListService, times(1)).removeItemFromWishList(userTest.getId(), itemId);
    }
}
