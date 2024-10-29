package com.diachenko.backend.application.services;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.dtos.CategoryDto;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long id);

    CategoryDto getCategoryDtoById(Long id);

    List<CategoryDto> getAllCategoriesList();

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto deleteCategory(Long id);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

}
