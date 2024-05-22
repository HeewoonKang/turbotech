package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.dto.DealerAgentDto;
import com.example.articleboard.articleboard.domain.dto.MediaDto;
import com.example.articleboard.articleboard.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Media")
public class MediaController {
    private final MediaService mediaService;
    @PutMapping(value = "/updateMedia/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateMedia(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        MediaDto mediaDto = new MediaDto();
        mediaDto.setTitle(title);
        mediaDto.setContent(content);
        mediaDto.setSource(source);
        mediaDto.setTag(tag);
        mediaDto.setCountry(country);

        boolean isUpdated = mediaService.updateMedia(id, mediaDto, imageFile);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/saveMedia", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveDealer(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        MediaDto mediaDto = new MediaDto();
        // DTO 필드 설정
        mediaDto.setTitle(title);
        mediaDto.setContent(content);
        mediaDto.setSource(source);
        mediaDto.setTag(tag);
        mediaDto.setCountry(country);

        boolean isSaved = mediaService.saveMedia(mediaDto, imageFile);
        if(isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
