package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.core.services.CategoryServiceImpl;
import com.diachenko.backend.dtos.CategoryDto;
import com.diachenko.backend.infrastructure.repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    CategoryDto categoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

    @MockBean
    private CategoryServiceImpl categoryService;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

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
        when(categoryService.getAllCategoriesList()).thenReturn(List.of(categoryDto));

        mockMvc.perform(get("/category/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("testing category name"))
                .andExpect(jsonPath("$[0].description").value("testing category desc"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testCreateCategory() throws Exception {
        CategoryDto categoryDtoTest = new CategoryDto(1L, "testing category name", "testing category desc");
        CategoryDto createdCategoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(categoryService.addCategory(categoryDto)).thenReturn(createdCategoryDto);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDtoTest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("testing category name"))
                .andExpect(header().string("Location", "/category/1"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testUpdateCategory() throws Exception {
        CategoryDto categoryDtoTest = new CategoryDto(2L, "testing category name", "testing category desc");
        CategoryDto createdCategoryDto = new CategoryDto(2L, "testing category name UPDATED", "testing category desc");

        when(categoryService.updateCategory(categoryDtoTest.getId(), categoryDtoTest)).thenReturn(createdCategoryDto);

        mockMvc.perform(put("/category/{id}", categoryDtoTest.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(categoryDtoTest))
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("testing category name UPDATED"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testDeleteCategory() throws Exception {
        CategoryDto categoryDtoTest = new CategoryDto(2L, "testing category name", "testing category desc");
        CategoryDto createdCategoryDto = new CategoryDto(2L, "testing category name UPDATED", "testing category desc");

        when(categoryService.deleteCategory(categoryDtoTest.getId())).thenReturn(createdCategoryDto);
        mockMvc.perform(delete("/category/{id}", categoryDtoTest.getId())
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("testing category name UPDATED"));
    }
}
