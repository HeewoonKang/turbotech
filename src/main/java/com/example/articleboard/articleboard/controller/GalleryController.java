package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.dto.DealerAgentDto;
import com.example.articleboard.articleboard.domain.dto.GalleryDto;
import com.example.articleboard.articleboard.service.DealerAgentService;
import com.example.articleboard.articleboard.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Gallery")
public class GalleryController {
    private final GalleryService galleryService;

    @PutMapping(value = "/updateGallery/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateGallery(
            @PathVariable("id") Long id,
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("copyright") String copyright,
            @RequestParam("tag") String tag,
            @RequestParam("resolution") String resolution,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        GalleryDto galleryDto = new GalleryDto();
        galleryDto.setProductName(productName);
        galleryDto.setDescription(description);
        galleryDto.setCopyright(copyright);
        galleryDto.setTag(tag);
        galleryDto.setResolution(resolution);

        boolean isUpdated = galleryService.updateGallery(id, galleryDto, imageFile);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/saveGallery", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveDealer(
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("copyright") String copyright,
            @RequestParam("tag") String tag,
            @RequestParam("resolution") String resolution,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        GalleryDto galleryDto = new GalleryDto();
        // DTO 필드 설정
        galleryDto.setProductName(productName);
        galleryDto.setDescription(description);
        galleryDto.setCopyright(copyright);
        galleryDto.setTag(tag);
        galleryDto.setResolution(resolution);

        boolean isSaved = galleryService.saveGallery(galleryDto, imageFile);
        if(isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
