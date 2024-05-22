package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.domain.Video;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.VideoRepository;
import com.example.articleboard.articleboard.service.VideoService;
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
public class VideoPageController {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    @GetMapping("/membership/Video/edit/{id}")
    public String editDealer(@PathVariable long id, Model model)
    {
        Video video= videoRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("video", videoService.getVideo(id));
        model.addAttribute("postId", videoRepository.findById(id));

        return "th/Turbo/turboVideoUpdate";
    }

    @GetMapping("/membership/Video/insert")
    public String GalleryInsert() {
        return "th/Turbo/turboVideoInsert";
    }

    @GetMapping("/membership/Video/{id}")
    public String  getDealerAgent(@PathVariable long id, Model model)
    {
        Video video = videoRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("video", videoService.getVideo(id));

        String nickName = video.getUser().getNickName();
        model.addAttribute("nickName", nickName);

        return "th/Turbo/turboVideoDetail";
    }

    @GetMapping("/membership/Video")
    public String GalleryPage(Model model,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Video> videoList = videoService.getVideoList(pageable);
        long count = videoService.countVideo();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", videoList);
        return "th/Turbo/turboVideo";
    }

    @GetMapping("/membership/Video/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDealerAgents(@RequestParam("category") String category,
                                                                  @RequestParam("keyword") String keyword,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Video> videoList;
        if (keyword == null || keyword.isEmpty()) {
            videoList = videoRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "product":
                    videoList = videoRepository.findByProductNameContaining(keyword, pageable);
                    break;
                case "author":
                    videoList = videoRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    videoList = videoRepository.findByProductNameContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    videoList = videoRepository.findAll(pageable);
                    break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", videoList.getContent());
        response.put("last", videoList.isLast());
        response.put("totalPosts", videoList.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
