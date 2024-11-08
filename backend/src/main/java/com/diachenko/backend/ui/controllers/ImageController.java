package com.diachenko.backend.ui.controllers;

import com.diachenko.backend.core.entities.Image;
import com.diachenko.backend.core.services.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/*  E-commerce-shop
    30.10.2024
    @author DiachenkoDanylo
*/
@RestController
@RequestMapping("/images/")
@RequiredArgsConstructor
public class ImageController {

    private final ImageServiceImpl imageService;

    @GetMapping("{itemId}")
    public ResponseEntity<List<Image>> getImagesByItemId(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(imageService.getListImageFromItem(itemId));
    }

    @PostMapping("{itemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveImage(@PathVariable("itemId") Long itemId, @RequestParam("file") MultipartFile file)
            throws IOException, URISyntaxException {
        return ResponseEntity.ok(imageService.saveImageToItem(itemId, file.getBytes()));
    }

    @DeleteMapping("{itemId}") // <-if all != true  -> itemId = imageId
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteImageById(@PathVariable("itemId") Long itemId,
                                            @RequestParam(name = "all", required = false) Boolean all) {
        if (Boolean.TRUE.equals(all)){
            return ResponseEntity.ok(imageService.deleteAllImagesFromItemById(itemId));
        } else {
            return ResponseEntity.ok(imageService.deleteImageById(itemId));
        }
    }
}
