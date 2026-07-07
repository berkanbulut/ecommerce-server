package com.example.demo.product.controller;

import com.example.demo.product.service.ImageUploadService;
import com.example.demo.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/images")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    public ImageUploadController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PreAuthorize("hasAuthority('image:upload')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadProductImage(
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = imageUploadService.uploadImage(file);

        return ResponseEntity.ok(
                ApiResponse.success("Image uploaded successfully", imageUrl)
        );
    }
}