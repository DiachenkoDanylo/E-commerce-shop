package com.diachenko.backend.core.services;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.dtos.CategoryDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.CategoryMapper;
import com.diachenko.backend.infrastructure.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository repository;
    @Mock
    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategoryById() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        when(repository.findById(category.getId())).thenReturn(Optional.of(category));

        assertEquals(categoryService.getCategoryById(category.getId()), category);
    }

    @Test
    void testGetCategoryById_AppException() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        when(repository.findById(category.getId())).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        assertEquals("Category with id:" + category.getId() + " was not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryDtoById() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        CategoryDto categoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(repository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        assertEquals(categoryService.getCategoryDtoById(category.getId()), categoryDto);
    }

    @Test
    void testGetAllCategoriesList() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        CategoryDto categoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(repository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtos(List.of(category))).thenReturn(List.of(categoryDto));

        assertEquals(List.of(categoryDto), categoryService.getAllCategoriesList());
    }

    @Test
    void testGetAllCategoriesList_AppException() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        AppException thrown = assertThrows(AppException.class, () -> {
            categoryService.getAllCategoriesList();
        });

        assertEquals("There is no category yet", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testAddCategory() {
        Category category = new Category(null, "testing category name", "testing category desc");
        CategoryDto categoryDto = new CategoryDto(null, "testing category name", "testing category desc");

        Category savedCategory = new Category(1L, "testing category name", "testing category desc");
        CategoryDto savedCategoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(categoryMapper.toCategory(categoryDto)).thenReturn(category);
        when(repository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toCategoryDto(savedCategory)).thenReturn(savedCategoryDto);

        assertEquals(categoryService.addCategory(categoryDto), savedCategoryDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void testDeleteCategory() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        CategoryDto categoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(repository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        assertEquals(categoryService.deleteCategory(1L), categoryDto);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_AppException() {
        Category category = new Category(1L, "testing category name", "testing category desc");
        CategoryDto categoryDto = new CategoryDto(1L, "testing category name", "testing category desc");

        when(repository.findById(category.getId())).thenReturn(Optional.empty());

        AppException thrown = assertThrows(AppException.class, () -> {
            categoryService.deleteCategory(category.getId());
        });

        assertEquals("category not found", thrown.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
        verify(repository, times(1)).findById(anyLong());
    }

//    @Override
//    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
//        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException("category not found", HttpStatus.NOT_FOUND));
//        categoryMapper.updateCategory(category, categoryMapper.toCategory(categoryDto));
//
//        Category updatedCategory = categoryRepository.save(category);
//
//        return categoryMapper.toCategoryDto(updatedCategory);
//    }


}
