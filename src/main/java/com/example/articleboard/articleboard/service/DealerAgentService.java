package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.DealerAgentDto;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBePosted;
import com.example.articleboard.articleboard.exception.custom.UserNotFoundException;
import com.example.articleboard.articleboard.repository.DealerAgentRepository;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.security.auth.AuthFacade;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
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
public class DealerAgentService {
    private final DealerAgentRepository dealerAgentRepository;
    private final UserRepository userRepository;
    private final AuthFacade authFacade;
    private final JwtProvider jwtProvider;

    private static final String IMAGE_DIRECTORY = "/home/ec2-user/turbotech/DealerAgent/";

    @Transactional(readOnly = true)
    public long countDealerAgent() {
        return dealerAgentRepository.count();
    }

    @Transactional(readOnly = true)
    public DealerAgent getDealerAgent(Long id) {
        return dealerAgentRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<DealerAgent> getDealerAgentList(Pageable pageable) {
        return dealerAgentRepository.findAll(pageable);
    }

    public boolean updateDealerAgent(Long id, DealerAgentDto dealerAgentDto, MultipartFile imageFile) {
        DealerAgent dealerAgent = dealerAgentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DealerAgent not found"));

        dealerAgent.setTitle(dealerAgentDto.getTitle());
        dealerAgent.setSource(dealerAgentDto.getSource());
        dealerAgent.setContent(dealerAgentDto.getContent());
        dealerAgent.setCountry(dealerAgentDto.getCountry());
        dealerAgent.setLocation(dealerAgentDto.getLocation());
        dealerAgent.setTag(dealerAgentDto.getTag());

        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dealerAgent.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 사용자 정보는 수정하지 않고 생성 및 수정 시간만 업데이트
        dealerAgent.setUpdatedAt(LocalDateTime.now());

        log.info("updateDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updateDealerAgent >>> DealerAgentInfo: {}", dealerAgent);

        dealerAgentRepository.save(dealerAgent);
        return true;
    }

    /*public boolean saveDealerAgent(DealerAgentDto dealerAgentDto)
    {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(UserNotFoundException::new);

        DealerAgent dealerAgent = new DealerAgent();
        dealerAgent.setTitle(dealerAgentDto.getTitle());
        dealerAgent.setSource(dealerAgentDto.getSource());
        dealerAgent.setContent(dealerAgentDto.getContent());
        dealerAgent.setCountry(dealerAgentDto.getCountry());
        dealerAgent.setLocation(dealerAgentDto.getLocation());
        dealerAgent.setTag(dealerAgentDto.getTag());
        dealerAgent.setImagePath(dealerAgentDto.getImagePath());
        dealerAgent.setUser(findUser);

        dealerAgent.setCreateAt(LocalDateTime.now());
        dealerAgent.setUpdatedAt(LocalDateTime.now());

        log.info("saveDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("saveDealerAgent >>> DealerAgentInfo: {}", dealerAgent);

        Long dealerAgentId = dealerAgentRepository.save(dealerAgent).getId();
        if(dealerAgentId > 0) return true;
        else throw new PostCouldNotBePosted();
    }*/
    public boolean saveDealerAgent(DealerAgentDto dealerAgentDto, MultipartFile imageFile) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        DealerAgent dealerAgent = new DealerAgent();
        dealerAgent.setTitle(dealerAgentDto.getTitle());
        dealerAgent.setSource(dealerAgentDto.getSource());
        dealerAgent.setContent(dealerAgentDto.getContent());
        dealerAgent.setCountry(dealerAgentDto.getCountry());
        dealerAgent.setLocation(dealerAgentDto.getLocation());
        dealerAgent.setTag(dealerAgentDto.getTag());
        // 이미지 처리 추가
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dealerAgent.setImagePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }
        /*if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(IMAGE_DIRECTORY + fileName);
            try {
                Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                dealerAgent.setImagePath(path.toString());
            } catch (IOException e) {
                e.printStackTrace();

            }
        }*/

        dealerAgent.setUser(findUser);
        dealerAgent.setCreateAt(LocalDateTime.now());
        dealerAgent.setUpdatedAt(LocalDateTime.now());

        log.info("saveDealerAgent >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("saveDealerAgent >>> DealerAgentInfo: {}", dealerAgent);

        Long dealerAgentId = dealerAgentRepository.save(dealerAgent).getId();
        return dealerAgentId > 0;
    }
}
