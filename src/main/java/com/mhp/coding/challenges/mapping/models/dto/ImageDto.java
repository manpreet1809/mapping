package com.mhp.coding.challenges.mapping.models.dto;

import com.mhp.coding.challenges.mapping.models.db.ImageSize;

public class ImageDto {

    private Long id;

    private String url;

    private ImageSize imageSize;

    public ImageDto(Long id, String url, ImageSize imageSize) {
        this.id = id;
        this.url = url;
        this.imageSize = imageSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
