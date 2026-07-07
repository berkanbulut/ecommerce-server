package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {
    @Value("${app.upload.base-path}")
    private String basePath;

    @Value("${app.upload.product-path}")
    private String productPath;

    public Path getProductUploadPath() {
        return Paths.get(basePath, productPath).toAbsolutePath().normalize();
    }
}
