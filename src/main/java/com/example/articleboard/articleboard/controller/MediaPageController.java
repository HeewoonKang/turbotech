package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Media;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.MediaRepository;
import com.example.articleboard.articleboard.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MediaPageController {
    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping("/membership/Media/edit/{id}")
    public String editMedia(@PathVariable long id, Model model)
    {
        Media media = mediaRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("media", mediaService.getMedia(id));
        model.addAttribute("postId", mediaRepository.findById(id));

        return "th/Turbo/turboMediaUpdate";
    }

    @GetMapping("/membership/Media/insert")
    public String mediaInsert() {
        return "th/Turbo/turboMediaInsert";
    }

    @GetMapping("/membership/Media/{id}")
    public String  getMedia(@PathVariable long id, Model model)
    {
        Media media = mediaRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("media", mediaService.getMedia(id));

        String nickName = media.getUser().getNickName();
        model.addAttribute("nickName", nickName);

        return "th/Turbo/turboMediaDetail";
    }

    @GetMapping("/membership/Media")
    public String DealerAgentPage(Model model,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Media> mediaList = mediaService.getMediaList(pageable);
        long count = mediaService.countMedia();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", mediaList);
        return "th/Turbo/turboMedia";
    }

    @GetMapping("/membership/Media/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDealerAgents(@RequestParam("category") String category,
                                                                  @RequestParam("keyword") String keyword,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Media> mediaList;
        if (keyword == null || keyword.isEmpty()) {
            mediaList = mediaRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "title":
                    mediaList = mediaRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "author":
                    mediaList = mediaRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    mediaList = mediaRepository.findByTitleContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    mediaList = mediaRepository.findAll(pageable);
                    break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", mediaList.getContent());
        response.put("last", mediaList.isLast());
        response.put("totalPosts", mediaList.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
