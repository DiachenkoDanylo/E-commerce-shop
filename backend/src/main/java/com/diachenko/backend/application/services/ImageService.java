package com.diachenko.backend.application.services;
/*  E-commerce-shop
    31.10.2024
    @author DiachenkoDanylo
*/

import com.diachenko.backend.core.entities.Image;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ImageService {

    String getImageFormat(byte[] file) throws IOException;

    String saveImageToItem(Long itemId, byte[] file) throws URISyntaxException, IOException;

    List<Image> getListImageFromItem(Long itemId);

    Image deleteImageById(Long imageId);

    String deleteAllImagesFromItemById(Long itemId);
}
