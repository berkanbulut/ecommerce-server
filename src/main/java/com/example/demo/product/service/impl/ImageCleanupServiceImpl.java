package com.example.demo.product.service.impl;

import com.example.demo.config.FileStorageConfig;
import com.example.demo.product.service.ImageCleanupService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageCleanupServiceImpl implements ImageCleanupService {

    private final FileStorageConfig fileStorageConfig;

    public ImageCleanupServiceImpl(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    @Override
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {
            String filename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            Path imagePath = fileStorageConfig
                    .getProductUploadPath()
                    .resolve(filename)
                    .normalize();

            Files.deleteIfExists(imagePath);

        } catch (Exception ignored) {
        }
    }
}