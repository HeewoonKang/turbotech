package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.GalleryRepository;
import com.example.articleboard.articleboard.service.GalleryService;
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
public class GalleryPageController {
    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryRepository galleryRepository;

    @GetMapping("/membership/Gallery/edit/{id}")
    public String editDealer(@PathVariable long id, Model model)
    {
        Gallery gallery= galleryRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("gallery", galleryService.getGallery(id));
        model.addAttribute("postId", galleryRepository.findById(id));

        return "th/Turbo/turboGalleryUpdate";
    }

    @GetMapping("/membership/Gallery/insert")
    public String GalleryInsert() {
        return "th/Turbo/turboGalleryInsert";
    }

    @GetMapping("/membership/Gallery/{id}")
    public String  getDealerAgent(@PathVariable long id, Model model)
    {
        Gallery gallery = galleryRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("gallery", galleryService.getGallery(id));

        String nickName = gallery.getUser().getNickName();
        model.addAttribute("nickName", nickName);

        return "th/Turbo/turboGalleryDetail";
    }

    /*@GetMapping("/membership/DealerAgent/more")
    public ResponseEntity<Map<String, Object>> dealerAgentMore(@RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<DealerAgent> dealerAgentList = dealerAgentService.getDealerAgentList(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", dealerAgentList.getContent());
        response.put("last", dealerAgentList.isLast());

        return ResponseEntity.ok(response);
    }*/

    @GetMapping("/membership/Gallery")
    public String GalleryPage(Model model,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Gallery> galleryList = galleryService.getGalleryList(pageable);
        long count = galleryService.countGallery();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", galleryList);
        return "th/Turbo/turboGallery";
    }

    @GetMapping("/membership/Gallery/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDealerAgents(@RequestParam("category") String category,
                                                                  @RequestParam("keyword") String keyword,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Gallery> galleryList;
        if (keyword == null || keyword.isEmpty()) {
            galleryList = galleryRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "product":
                    galleryList = galleryRepository.findByProductNameContaining(keyword, pageable);
                    break;
                case "author":
                    galleryList = galleryRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    galleryList = galleryRepository.findByProductNameContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    galleryList = galleryRepository.findAll(pageable);
                    break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", galleryList.getContent());
        response.put("last", galleryList.isLast());
        response.put("totalPosts", galleryList.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
