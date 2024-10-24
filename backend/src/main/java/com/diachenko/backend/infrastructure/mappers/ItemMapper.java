package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    30.09.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.dtos.ItemDto;
import com.diachenko.backend.core.entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Item.class})
public interface ItemMapper {

    Item toItem(ItemDto itemDto);

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtos(List<Item> itemList);

    void updateItem(@MappingTarget Item target, Item source);

}
