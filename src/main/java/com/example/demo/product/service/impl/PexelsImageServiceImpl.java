package com.example.demo.product.service.impl;

import com.example.demo.product.dto.PexelsSearchResponse;
import com.example.demo.product.service.PexelsImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PexelsImageServiceImpl implements PexelsImageService {

    private final RestClient restClient;

    public PexelsImageServiceImpl(
            @Value("${pexels.api-key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.pexels.com/v1")
                .defaultHeader("Authorization", apiKey)
                .build();
    }

    @Override
    public List<String> searchImageUrls(String query, int count) {

        if (query == null || query.isBlank() || count <= 0) {
            return List.of();
        }

        try {
            PexelsSearchResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("query", query)
                            .queryParam("per_page", count)
                            .queryParam("orientation", "square")
                            .build())
                    .retrieve()
                    .body(PexelsSearchResponse.class);

            if (response == null || response.photos() == null) {
                return List.of();
            }

            return response.photos().stream()
                    .filter(photo -> photo.src() != null)
                    .map(photo -> photo.src().large())
                    .filter(url -> url != null && !url.isBlank())
                    .limit(count)
                    .toList();

        } catch (Exception exception) {
            System.err.println(
                    "Pexels images could not be retrieved for query: "
                            + query
                            + ". Error: "
                            + exception.getMessage()
            );

            return List.of();
        }
    }
}