package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.Media;
import com.example.articleboard.articleboard.domain.Newsletter;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.NewsletterRepository;
import com.example.articleboard.articleboard.service.NewsletterService;
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
public class NewsletterPageController {
    @Autowired
    private NewsletterService newsletterService;

    @Autowired
    private NewsletterRepository newsletterRepository;

    @GetMapping("/membership/Newsletter/edit/{id}")
    public String editMedia(@PathVariable long id, Model model)
    {
        Newsletter newsletter = newsletterRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("newsletter", newsletterService.getMedia(id));
        model.addAttribute("postId", newsletterRepository.findById(id));

        return "th/Turbo/turboNewsletterUpdate";
    }

    @GetMapping("/membership/Newsletter/insert")
    public String mediaInsert() {
        return "th/Turbo/turboNewsletterInsert";
    }

    @GetMapping("/membership/Newsletter/{id}")
    public String  getMedia(@PathVariable long id, Model model)
    {
        Newsletter newsletter = newsletterRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        model.addAttribute("newsletter", newsletterService.getMedia(id));

        String nickName = newsletter.getUser().getNickName();
        model.addAttribute("nickName", nickName);

        return "th/Turbo/turboNewsletterDetail";
    }

    @GetMapping("/membership/Newsletter")
    public String DealerAgentPage(Model model,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Newsletter> newsletterList = newsletterService.getMediaList(pageable);
        long count = newsletterService.countNewsletter();
        model.addAttribute("totalPosts", count);
        model.addAttribute("posts", newsletterList);
        return "th/Turbo/turboNewsletter";
    }

    @GetMapping("/membership/Newsletter/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchDealerAgents(@RequestParam("category") String category,
                                                                  @RequestParam("keyword") String keyword,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Newsletter> newsletterList;
        if (keyword == null || keyword.isEmpty()) {
            newsletterList = newsletterRepository.findAll(pageable);    // 검색어 입력 안하믄 전체 출력
        } else {

            switch (category) {
                case "title":
                    newsletterList = newsletterRepository.findByTitleContaining(keyword, pageable);
                    break;
                case "author":
                    newsletterList = newsletterRepository.findByUser_NickNameContaining(keyword, pageable);
                    break;
                case "all":
                    newsletterList = newsletterRepository.findByTitleContainingOrUser_NickNameContaining(keyword, keyword, pageable);
                    break;
                default:
                    newsletterList = newsletterRepository.findAll(pageable);
                    break;
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", newsletterList.getContent());
        response.put("last", newsletterList.isLast());
        response.put("totalPosts", newsletterList.getTotalElements());

        return ResponseEntity.ok(response);
    }
}
