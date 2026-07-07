package com.example.demo.product.service.impl;

import com.example.demo.config.FileStorageConfig;
import com.example.demo.exception.BadRequestException;
import com.example.demo.product.service.ImageUploadService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "webp"
    );

    private final FileStorageConfig fileStorageConfig;

    public ImageUploadServiceImpl(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    @Override
    public String uploadImage(MultipartFile file) {

        validateFile(file);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = UUID.randomUUID() + "." + fileExtension;

        Path uploadPath = fileStorageConfig.getProductUploadPath();

        try {
            Files.createDirectories(uploadPath);

            Path targetLocation = uploadPath.resolve(newFilename);

            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/products/" + newFilename;

        } catch (Exception e) {
            throw new BadRequestException("Could not store file. Please try again");
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size must not exceed 10MB");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(originalFilename);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BadRequestException("Only jpg, jpeg, png and webp files are allowed");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BadRequestException("File extension is required");
        }

        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}