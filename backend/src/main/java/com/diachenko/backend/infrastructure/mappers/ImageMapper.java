package com.diachenko.backend.infrastructure.mappers;
/*  E-commerce-shop
    31.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Image;
import com.diachenko.backend.dtos.ImageDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {Image.class})
public interface ImageMapper {

    ImageDto toImageDto(Image image);

    Image toImage(ImageDto imageDto);

    List<ImageDto> toImageDtoList(List<Image> imageList);

    List<Image> toImageList(List<ImageDto> imageDtoList);
}
