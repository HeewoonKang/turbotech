package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.dto.NewsletterDto;
import com.example.articleboard.articleboard.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Newsletter")
public class NewsletterController {
    private final NewsletterService newsletterService;
    @PutMapping(value = "/updateNewsletter/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateNewsletter(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        NewsletterDto newsletterDto = new NewsletterDto();
        newsletterDto.setTitle(title);
        newsletterDto.setContent(content);
        newsletterDto.setSource(source);
        newsletterDto.setTag(tag);
        newsletterDto.setCountry(country);

        boolean isUpdated = newsletterService.updateMedia(id, newsletterDto, imageFile);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/saveNewsletter", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveNewsletter(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        NewsletterDto newsletterDto = new NewsletterDto();
        // DTO 필드 설정
        newsletterDto.setTitle(title);
        newsletterDto.setContent(content);
        newsletterDto.setSource(source);
        newsletterDto.setTag(tag);
        newsletterDto.setCountry(country);

        boolean isSaved = newsletterService.saveMedia(newsletterDto, imageFile);
        if(isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



}
