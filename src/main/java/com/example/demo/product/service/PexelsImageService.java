package com.example.demo.product.service;

import java.util.List;

public interface PexelsImageService {

    List<String> searchImageUrls(String query, int count);
}