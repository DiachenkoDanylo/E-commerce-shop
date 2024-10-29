package com.diachenko.backend.core.services;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.CategoryService;
import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.dtos.CategoryDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.CategoryMapper;
import com.diachenko.backend.infrastructure.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new AppException("Category with id:" + id + " was not found",
                HttpStatus.NOT_FOUND));
    }

    @Override
    public CategoryDto getCategoryDtoById(Long id) {
        return categoryMapper.toCategoryDto(getCategoryById(id));
    }

    @Override
    public List<CategoryDto> getAllCategoriesList() {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()){
            return  categoryMapper.toCategoryDtos(categories);
        }else {
            throw new AppException("There is no category yet",HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);

        Category createdCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    public CategoryDto deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException("category not found", HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
        return categoryMapper.toCategoryDto(category);

    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException("category not found", HttpStatus.NOT_FOUND));
        categoryMapper.updateCategory(category, categoryMapper.toCategory(categoryDto));

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDto(updatedCategory);
    }
}
