package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.dto.GalleryDto;
import com.example.articleboard.articleboard.domain.dto.VideoDto;
import com.example.articleboard.articleboard.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Video")
public class VideoController {
    private final VideoService videoService;

    @PostMapping(value = "/updateVideo/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateGallery(
            @PathVariable("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("copyright") String copyright,
            @RequestParam("tag") String tag,
            @RequestParam("resolution") String resolution,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile videoFile) {

        VideoDto videoDto = new VideoDto();
        videoDto.setProductName(productName);
        videoDto.setDescription(description);
        videoDto.setCopyright(copyright);
        videoDto.setTag(tag);
        videoDto.setResolution(resolution);

        boolean isUpdated = videoService.updateVideo(id, videoDto, imageFile, videoFile);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/saveVideo", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveDealer(
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("copyright") String copyright,
            @RequestParam("tag") String tag,
            @RequestParam("resolution") String resolution,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(value = "video", required = false) MultipartFile videoFile) {

        VideoDto videoDto = new VideoDto();
        videoDto.setProductName(productName);
        videoDto.setDescription(description);
        videoDto.setCopyright(copyright);
        videoDto.setTag(tag);
        videoDto.setResolution(resolution);

        boolean isSaved = videoService.saveVideo(videoDto, imageFile, videoFile);
        if(isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
