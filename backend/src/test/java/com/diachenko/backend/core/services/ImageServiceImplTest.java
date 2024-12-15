package com.diachenko.backend.core.services;
/*  E-commerce-shop
    01.11.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Image;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.ImageDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.ImageMapper;
import com.diachenko.backend.infrastructure.repositories.ImageRepository;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {


    @InjectMocks
    private ImageServiceImpl imageService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemServiceImpl itemService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageMapper imageMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetImageFormat_JPEG() throws IOException {
        Path.of("src/test/resources/test2.jpeg");
        byte[] jpegBytes = Files.readAllBytes(Path.of("src/test/resources/test2.jpg"));
        String format = imageService.getImageFormat(jpegBytes);
        assertEquals("JPEG", format.toUpperCase());
    }

    @Test
    void testGetImageFormat_PNG() throws IOException {
        byte[] pngBytes = Files.readAllBytes(Path.of("src/test/resources/test1.png"));
        String format = imageService.getImageFormat(pngBytes);
        assertEquals("PNG", format.toUpperCase());
    }

    @Test
    void testGetImageFormat_UnsupportedFormat() {
        byte[] unsupportedBytes = "not_an_image".getBytes();
        assertThrows(IOException.class, () -> imageService.getImageFormat(unsupportedBytes));
    }

    @Test
    void testGetListImageFromItem() {
        Category category = new Category(1L, "testing category", "testing description");
        Item item1 = new Item(1L, "testItem1", category, "testDesc1", 100, null, 10);
        ImageDto imageDto = new ImageDto(1L, "images/items/1/1.jpeg");
        Image image = new Image(1L, "images/items/1/1.jpeg", item1);
        item1.setImages(List.of(image));

        when(itemService.findItemById(1L)).thenReturn(item1);
        when(imageMapper.toImageDtoList(anyList())).thenReturn(List.of(imageDto));
        assertEquals(imageService.getListImageFromItem(1L), List.of(imageDto));
    }

    @Test
    void testGetListImageFromItem_AppException() {
        when(itemService.findItemById(1L)).thenThrow(new AppException("item not found", HttpStatus.NOT_FOUND));
        AppException thrown = assertThrows(AppException.class, () -> {
            itemService.findItemById(1L);
        });
        assertEquals("item not found", thrown.getMessage());
    }

    @Test
    void testDeleteImageById() {
        Category category = new Category(1L, "testing category", "testing description");
        Item item1 = new Item(1L, "testItem1", category, "testDesc1", 100, null, 10);
        Image image = new Image(1L, "images/items/1/1.jpeg", item1);
        ImageDto imageDto = new ImageDto(1L, "images/items/1/1.jpeg");
        item1.setImages(List.of(image));

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(imageMapper.toImageDto(any())).thenReturn(imageDto);
        assertEquals(imageService.deleteImageById(1L), imageDto);

        verify(imageRepository, times(1)).deleteById(image.getId());
    }

    @Test
    void testDeleteImageById_AppException() {
        when(imageRepository.findById(1L)).thenThrow(
                new AppException("ImageDto with id =" + 1L + "was not found", HttpStatus.NOT_FOUND));

        AppException thrown = assertThrows(AppException.class, () -> {
            imageRepository.findById(1L);
        });

        assertEquals(thrown.getMessage(), "ImageDto with id =" + 1L + "was not found");
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void testDeleteAllImagesFromItem() {
        Category category = new Category(1L, "testing category", "testing description");
        Item item1 = new Item(2L, "testItem1", category, "testDesc1", 100, null, 10);
        Image image = new Image(1L, "images/items/1/1.jpeg", item1);
        item1.setImages(List.of(image));
        long itemId = item1.getId();
        Item updatedItem1 = new Item(2L, "testItem1", category, "testDesc1", 100, new ArrayList<>(), 10);

        when(itemService.findItemById(itemId)).thenReturn(item1);

        assertEquals(imageService.deleteAllImagesFromItemById(itemId), "Images from item with id =" + itemId + " was deleted successfully");
        verify(imageRepository, times(1)).deleteAllByItem_Id(itemId);
        verify(itemRepository, times(1)).save(updatedItem1);
    }
}
