package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    30.09.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Category;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.core.services.CategoryServiceImpl;
import com.diachenko.backend.dtos.ItemDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface ItemMapper {

    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    Item toItem(ItemDto itemDto, @Context CategoryServiceImpl categoryService);

    @Mapping(target = "categoryId", source = "category.id")
    ItemDto toItemDto(Item item);

    @Named("mapCategory")
    default Category mapCategory(Long categoryId, @Context CategoryServiceImpl categoryService) {
        return categoryId != null ? categoryService.getCategoryById(categoryId) : null;
    }

    List<ItemDto> toItemDtos(List<Item> itemList);

    void updateItem(@MappingTarget Item target, Item source);

}
