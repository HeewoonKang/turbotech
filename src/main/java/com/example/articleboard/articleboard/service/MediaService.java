package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Media;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.DealerAgentDto;
import com.example.articleboard.articleboard.domain.dto.MediaDto;
import com.example.articleboard.articleboard.repository.MediaRepository;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.security.auth.AuthFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Transactional(readOnly = false)
@Slf4j
@RequiredArgsConstructor
public class MediaService {
    private final AuthFacade authFacade;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;

    private static final String IMAGE_DIRECTORY = "/home/ec2-user/turbotech/Media/";

    @Transactional(readOnly = true)
    public long countMedia() {
        return mediaRepository.count();
    }

    @Transactional(readOnly = true)
    public Media getMedia(Long id) {
        return mediaRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<Media> getMediaList(Pageable pageable) {
        return mediaRepository.findAll(pageable);
    }

    public boolean updateMedia(Long id, MediaDto mediaDto, MultipartFile imageFile) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DealerAgent not found"));

        media.setTitle(mediaDto.getTitle());
        media.setSource(mediaDto.getSource());
        media.setContent(mediaDto.getContent());
        media.setCountry(mediaDto.getCountry());
        media.setLocation(mediaDto.getLocation());
        media.setTag(mediaDto.getTag());

        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                media.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 사용자 정보는 수정하지 않고 생성 및 수정 시간만 업데이트
        media.setUpdatedAt(LocalDateTime.now());

        log.info("updateDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updateDealerAgent >>> DealerAgentInfo: {}", media);

        mediaRepository.save(media);
        return true;
    }
    public boolean saveMedia(MediaDto mediaDto, MultipartFile imageFile) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Media media = new Media();
        media.setTitle(mediaDto.getTitle());
        media.setSource(mediaDto.getSource());
        media.setContent(mediaDto.getContent());
        media.setCountry(mediaDto.getCountry());
        media.setLocation(mediaDto.getLocation());
        media.setTag(mediaDto.getTag());
        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                media.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }

        media.setUser(findUser);
        media.setCreateAt(LocalDateTime.now());
        media.setUpdatedAt(LocalDateTime.now());

        log.info("saveDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("saveDealerAgent >>> DealerAgentInfo: {}", media);

        Long dealerAgentId = mediaRepository.save(media).getId();
        return dealerAgentId > 0;
    }
}

