package com.diachenko.backend.ui.controllers;
/*  E-commerce-shop
    06.11.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Image;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.services.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageServiceImpl imageService;

    private static final String BASE_URI = "/images/";

    List<Image> testSetUp() {
        Category category = new Category(1L, "testing category", "testing description");
        Item item1 = new Item(1L, "testItem1", category, "testDesc1", 100, null, 10);
        Image image = new Image(1L, "images/items/1/1.jpeg", item1);
        item1.setImages(List.of(image));
        return List.of(image);
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{CLIENT}")
    void testGetImagesByItemId() throws Exception {
        List<Image> images = testSetUp();
        when(imageService.getListImageFromItem(1L)).thenReturn(images);

        mockMvc.perform(get(BASE_URI + "{itemDd}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].imageUrl").value("images/items/1/1.jpeg"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testSaveImage() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Sample Image Content".getBytes()
        );

        when(imageService.saveImageToItem(1L, file.getBytes())).thenReturn("images/items/1/1.jpeg");
        mockMvc.perform(multipart(BASE_URI + "{itemId}", 1L)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("images/items/1/1.jpeg"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testDeleteImageById_All() throws Exception {
        when(imageService.deleteAllImagesFromItemById(1L)).thenReturn("All images deleted for item 1");

        mockMvc.perform(delete(BASE_URI + "{itemId}", 1L)
                        .param("all", "true")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("All images deleted for item 1"));
    }

    @Test
    @WithMockUser(username = "testuser", authorities = "{ADMIN}")
    void testDeleteImageById_Single() throws Exception {

        when(imageService.deleteImageById(1L)).thenReturn(testSetUp().get(0));

        mockMvc.perform(delete(BASE_URI + "{itemId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(1L)));
    }

}
