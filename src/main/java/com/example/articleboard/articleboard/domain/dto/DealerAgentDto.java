package com.example.articleboard.articleboard.domain.dto;

import lombok.Data;

@Data
public class DealerAgentDto {
    private String title;
    private String content;
    private String imagePath;
    private String source;
    private String country;
    private String location;
    private String tag;
}
