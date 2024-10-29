package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    29.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.dtos.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Category.class})
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtos(List<Category> categoryList);

    void updateCategory(@MappingTarget Category target, Category source);


}
