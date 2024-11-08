package com.diachenko.backend.core.services;

import com.diachenko.backend.application.services.ImageService;
import com.diachenko.backend.core.entities.Image;
import com.diachenko.backend.core.entities.Item;
import com.diachenko.backend.exceptions.AppException;
import com.diachenko.backend.infrastructure.repositories.ImageRepository;
import com.diachenko.backend.infrastructure.repositories.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_PATH_ITEMS = "file:///app/images/items";
    private final ItemRepository itemRepository;
    private final ItemServiceImpl itemService;
    private final ImageRepository imageRepository;

    @Override
    public String getImageFormat(byte[] file) throws IOException {
        String formatName;
        InputStream is = new ByteArrayInputStream(file);
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (readers.hasNext()) {
            ImageReader reader = readers.next();
            formatName = reader.getFormatName(); // E.g., "JPEG", "PNG"
            reader.dispose();
        } else {
            throw new IOException("Unsupported image format.");
        }
        return formatName;
    }

    @Override
    public String saveImageToItem(Long itemId, byte[] file) throws URISyntaxException, IOException {
        // Find the item by its ID
        Item item = itemService.findItemById(itemId);

        // Define the URI for the item's directory (e.g., "/app/images/items/1")
        URI saveUri = new URI(IMAGE_PATH_ITEMS + "/" + item.getId());
        Path itemDirPath = Paths.get(saveUri);

        // Ensure the item's directory exists, or create it
        Files.createDirectories(itemDirPath);
        String formatName = getImageFormat(file);

        // Define the unique filename for the new image
        String imageName = String.valueOf(item.getImages().size() + 1);
        Path imagePath = itemDirPath.resolve(imageName + "." + formatName.toLowerCase());

        ByteArrayInputStream bis = new ByteArrayInputStream(file);
        BufferedImage imageBuff = ImageIO.read(bis);
        ImageIO.write(imageBuff, formatName, imagePath.toFile());
        Image image = new Image(null, imagePath.toFile().getAbsolutePath(), item);
        item.addImageToItem(image);
        itemRepository.save(item);
        return imagePath.toFile().getAbsolutePath();
    }

    @Override
    public List<Image> getListImageFromItem(Long itemId) {
        return itemService.findItemById(itemId).getImages();
    }

    @Override
    public Image deleteImageById(Long imageId) {
        Image imageToDelete = imageRepository.findById(imageId).orElseThrow(
                () -> new AppException("Image with id =" + imageId + "was not found", HttpStatus.NOT_FOUND));
        imageRepository.deleteById(imageId);
        return imageToDelete;
    }

    @Transactional
    @Override
    public String deleteAllImagesFromItemById(Long itemId) {
        Item item = itemService.findItemById(itemId);
        imageRepository.deleteAllByItem_Id(itemId);
        item.setImages(new ArrayList<>());
        itemRepository.save(item);
        return "Images from item with id =" + itemId + " was deleted successfully";
    }
}
