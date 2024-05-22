package com.example.articleboard.articleboard.domain.dto;

import lombok.Data;

@Data
public class VideoDto {
    private String productName;
    private String description;
    private String imagePath;
    private String videoPath;
    private String resolution;
    private String copyright;
    private String tag;
}
