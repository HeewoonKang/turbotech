package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.GalleryDto;
import com.example.articleboard.articleboard.repository.GalleryRepository;
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
public class GalleryService {
    private final AuthFacade authFacade;
    private final GalleryRepository galleryRepository;
    private final UserRepository userRepository;

    private static final String IMAGE_DIRECTORY = "/home/ec2-user/turbotech/Gallery/";

    @Transactional(readOnly = true)
    public long countGallery() {
        return galleryRepository.count();
    }

    @Transactional(readOnly = true)
    public Gallery getGallery(Long id) {
        return galleryRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<Gallery> getGalleryList(Pageable pageable) {
        return galleryRepository.findAll(pageable);
    }

    public boolean saveGallery(GalleryDto galleryDto, MultipartFile imageFile) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Gallery gallery = new Gallery();
        gallery.setProductName(galleryDto.getProductName());
        gallery.setDescription(galleryDto.getDescription());
        gallery.setResolution(galleryDto.getResolution());
        gallery.setCopyright(galleryDto.getCopyright());
        gallery.setTag(galleryDto.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                gallery.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }

        gallery.setUser(findUser);
        gallery.setCreateAt(LocalDateTime.now());
        gallery.setUpdatedAt(LocalDateTime.now());

        Long galleryId = galleryRepository.save(gallery).getId();
        return galleryId > 0;
    }

    public boolean updateGallery(Long id, GalleryDto galleryDto, MultipartFile imageFile) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery not found"));

        gallery.setProductName(galleryDto.getProductName());
        gallery.setDescription(galleryDto.getDescription());
        gallery.setResolution(galleryDto.getResolution());
        gallery.setCopyright(galleryDto.getCopyright());
        gallery.setTag(galleryDto.getTag());

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                gallery.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        gallery.setUpdatedAt(LocalDateTime.now());

        log.info("updateDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updateDealerAgent >>> DealerAgentInfo: {}", gallery);

        galleryRepository.save(gallery);
        return true;
    }
}
