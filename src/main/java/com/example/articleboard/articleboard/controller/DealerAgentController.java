package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Post;
import com.example.articleboard.articleboard.domain.dto.DealerAgentDto;
import com.example.articleboard.articleboard.repository.DealerAgentRepository;
import com.example.articleboard.articleboard.service.DealerAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/DealerAgent")
public class DealerAgentController {
    private final DealerAgentRepository dealerAgentRepository;
    private final DealerAgentService dealerAgentService;

    /*@PostMapping(value = "/saveDealer")
    public ResponseEntity<?> saveDealer(@RequestBody DealerAgentDto dealerAgentDto) {
        {
            boolean isSaved = dealerAgentService.saveDealerAgent(dealerAgentDto);
            if(isSaved) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }*/

  /*  @GetMapping("/details/{id}")
    public ResponseEntity<DealerAgent> getDealerAgentDetails(@PathVariable Long id) {
        DealerAgent dealerAgent = dealerAgentService.getDealerAgent(id);
        if (dealerAgent != null) {
            return ResponseEntity.ok(dealerAgent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
    @PutMapping(value = "/updateDealer/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateDealer(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        DealerAgentDto dealerAgentDto = new DealerAgentDto();
        dealerAgentDto.setTitle(title);
        dealerAgentDto.setContent(content);
        dealerAgentDto.setSource(source);
        dealerAgentDto.setTag(tag);
        dealerAgentDto.setCountry(country);

        boolean isUpdated = dealerAgentService.updateDealerAgent(id, dealerAgentDto, imageFile);
        if(isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/saveDealer", consumes = {"multipart/form-data"})
    public ResponseEntity<?> saveDealer(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("country") String country,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        DealerAgentDto dealerAgentDto = new DealerAgentDto();
        // DTO 필드 설정
        dealerAgentDto.setTitle(title);
        dealerAgentDto.setContent(content);
        dealerAgentDto.setSource(source);
        dealerAgentDto.setTag(tag);
        dealerAgentDto.setCountry(country);

        boolean isSaved = dealerAgentService.saveDealerAgent(dealerAgentDto, imageFile);
        if(isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}


