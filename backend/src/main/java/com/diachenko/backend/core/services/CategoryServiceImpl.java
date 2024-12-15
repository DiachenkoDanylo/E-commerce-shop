package com.diachenko.backend.core.services;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.application.services.CategoryService;
import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.dtos.CategoryDto;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.mappers.CategoryMapper;
import com.diachenko.backend.infrastructure.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    @Cacheable(value = "categoryDto",  key = "#p0")
    public CategoryDto getCategoryDtoById(Long id) {
        return categoryMapper.toCategoryDto(getCategoryById(id));
    }

    @Override
    @Cacheable(value = "allCategoriesPage")
    public Page<CategoryDto> getAllCategoriesList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        if (!categories.isEmpty()) {
            return new PageImpl<>(categoryMapper.toCategoryDtos(categories.getContent()), pageable, categories.getTotalPages());
        } else {
            throw new AppException("There is no category yet", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Caching(cacheable = {
            @Cacheable(value = "categoryDto", key = "#p0.id")
    })
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);

        Category createdCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    @CacheEvict(value = "categoryDto", key = "#p0")
    public CategoryDto deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException("category not found", HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
        return categoryMapper.toCategoryDto(category);

    }

    @Override
    @Caching(put = {
            @CachePut(value = "categoryDto", key = "#p0")
    })
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException("category not found", HttpStatus.NOT_FOUND));
        categoryMapper.updateCategory(category, categoryMapper.toCategory(categoryDto));

        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDto(updatedCategory);
    }
}
