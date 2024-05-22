package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.Media;
import com.example.articleboard.articleboard.domain.Newsletter;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.MediaDto;
import com.example.articleboard.articleboard.domain.dto.NewsletterDto;
import com.example.articleboard.articleboard.repository.NewsletterRepository;
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
public class NewsletterService {
    private final AuthFacade authFacade;
    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;

    private static final String IMAGE_DIRECTORY = "/home/ec2-user/turbotech/Newsletter/";

    @Transactional(readOnly = true)
    public long countNewsletter() {
        return newsletterRepository.count();
    }

    @Transactional(readOnly = true)
    public Newsletter getMedia(Long id) {
        return newsletterRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<Newsletter> getMediaList(Pageable pageable) {
        return newsletterRepository.findAll(pageable);
    }

    public boolean updateMedia(Long id, NewsletterDto newsletterDto, MultipartFile imageFile) {
        Newsletter newsletter = newsletterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DealerAgent not found"));

        newsletter.setTitle(newsletterDto.getTitle());
        newsletter.setSource(newsletterDto.getSource());
        newsletter.setContent(newsletterDto.getContent());
        newsletter.setCountry(newsletterDto.getCountry());
        newsletter.setLocation(newsletterDto.getLocation());
        newsletter.setTag(newsletterDto.getTag());

        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newsletter.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 사용자 정보는 수정하지 않고 생성 및 수정 시간만 업데이트
        newsletter.setUpdatedAt(LocalDateTime.now());

        log.info("updateDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updateDealerAgent >>> DealerAgentInfo: {}", newsletter);

        newsletterRepository.save(newsletter);
        return true;
    }
    public boolean saveMedia(NewsletterDto newsletterDto, MultipartFile imageFile) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        Newsletter newsletter = new Newsletter();
        newsletter.setTitle(newsletterDto.getTitle());
        newsletter.setSource(newsletterDto.getSource());
        newsletter.setContent(newsletterDto.getContent());
        newsletter.setCountry(newsletterDto.getCountry());
        newsletter.setLocation(newsletterDto.getLocation());
        newsletter.setTag(newsletterDto.getTag());
        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newsletter.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }

        newsletter.setUser(findUser);
        newsletter.setCreateAt(LocalDateTime.now());
        newsletter.setUpdatedAt(LocalDateTime.now());

        log.info("saveDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("saveDealerAgent >>> DealerAgentInfo: {}", newsletter);

        Long dealerAgentId = newsletterRepository.save(newsletter).getId();
        return dealerAgentId > 0;
    }
}
