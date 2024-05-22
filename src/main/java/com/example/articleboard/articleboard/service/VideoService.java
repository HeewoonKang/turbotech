package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.Video;
import com.example.articleboard.articleboard.domain.dto.GalleryDto;
import com.example.articleboard.articleboard.domain.dto.VideoDto;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.repository.VideoRepository;
import com.example.articleboard.articleboard.security.auth.AuthFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
@Transactional(readOnly = false)
@Slf4j
@RequiredArgsConstructor
public class VideoService {
    private final AuthFacade authFacade;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    private static final String VIDEO_DIRECTORY = "/home/ec2-user/turbotech/Video/Video/";
    private static final String IMAGE_DIRECTORY = "/home/ec2-user/turbotech/Video/Image/";

    @Transactional(readOnly = true)
    public long countVideo() {
        return videoRepository.count();
    }

    @Transactional(readOnly = true)
    public Video getVideo(Long id) {
        return videoRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<Video> getVideoList(Pageable pageable) {
        return videoRepository.findAll(pageable);
    }

    public boolean saveVideo(VideoDto videoDto, MultipartFile imageFile, MultipartFile videoFile) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Video video = new Video();
        video.setProductName(videoDto.getProductName());
        video.setDescription(videoDto.getDescription());
        video.setResolution(videoDto.getResolution());
        video.setCopyright(videoDto.getCopyright());
        video.setTag(videoDto.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                video.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoFileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path videoPath = Paths.get(VIDEO_DIRECTORY + videoFileName);
            try {
                Files.copy(videoFile.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
                video.setVideoPath(videoFileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }

        video.setUser(findUser);
        video.setCreateAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());

        Long videoId = videoRepository.save(video).getId();
        return videoId > 0;
    }
/*
    public boolean updateVideo(Long id, VideoDto videoDto, MultipartFile imageFile, MultipartFile videoFile) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("video not found"));

        video.setProductName(videoDto.getProductName());
        video.setDescription(videoDto.getDescription());
        video.setResolution(videoDto.getResolution());
        video.setCopyright(videoDto.getCopyright());
        video.setTag(videoDto.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                video.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoFileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path videoPath = Paths.get(VIDEO_DIRECTORY + videoFileName);
            try {
                Files.copy(videoFile.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
                video.setVideoPath(videoFileName); // Gallery 엔티티에 비디오 파일 경로 저장하는 메서드를 추가해야 함
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        video.setUpdatedAt(LocalDateTime.now());

        log.info("updateDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updateDealerAgent >>> DealerAgentInfo: {}", video);

        videoRepository.save(video);
        return true;
    }*/
public boolean updateVideo(Long id, VideoDto videoDto, MultipartFile imageFile, MultipartFile videoFile) {
    try {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        video.setProductName(videoDto.getProductName());
        video.setDescription(videoDto.getDescription());
        video.setResolution(videoDto.getResolution());
        video.setCopyright(videoDto.getCopyright());
        video.setTag(videoDto.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            video.setImagePath(fileName);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoFileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path videoPath = Paths.get(VIDEO_DIRECTORY + videoFileName);
            Files.copy(videoFile.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);
            video.setVideoPath(videoFileName);
        }

        video.setUpdatedAt(LocalDateTime.now());
        videoRepository.save(video);

        log.info("Video updated successfully: {}", video);
        return true;
    } catch (IOException e) {
        log.error("Error while processing file upload", e);
        return false;
    } catch (RuntimeException e) {
        log.error("Error while updating video", e);
        return false;
    } catch (Exception e) {
        log.error("Unexpected error occurred", e);
        return false;
    }
}
}
