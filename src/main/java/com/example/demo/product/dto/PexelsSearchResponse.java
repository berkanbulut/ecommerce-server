package com.example.demo.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PexelsSearchResponse(
        List<PexelsPhoto> photos
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PexelsPhoto(
            Long id,
            String alt,
            PexelsPhotoSource src
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PexelsPhotoSource(
            String original,
            String large,
            String large2x,
            String medium
    ) {
    }
}