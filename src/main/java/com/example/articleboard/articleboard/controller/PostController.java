package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.Post;
import com.example.articleboard.articleboard.domain.dto.PostDto;
import com.example.articleboard.articleboard.domain.dto.PostEditDto;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBeFound;
import com.example.articleboard.articleboard.repository.PostRepository;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
import com.example.articleboard.articleboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;

    // 다시 mvc로 돌리기 위한 주석
    /*@GetMapping("/list")
    public Page<Post> getPosts(@RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<Post> searchPosts(@RequestParam("title") String searchKeyword,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "3") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findByTitleContaining(searchKeyword, pageable);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getPost(@PathVariable long id)
    {
        Post post = postRepository.findById(id).orElseThrow(PostCouldNotBeFound::new);
        return ResponseEntity.ok(post);
    }*/

    /*@GetMapping("/api/post/count")
    public ResponseEntity<Long> getPostsCount() {
        long count = postService.countPost();
        return ResponseEntity.ok(count);
    }*/

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostEditDto postEditDto) {
        try {
            postService.updatePost(id, postEditDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping(value = "/savePost")
    public ResponseEntity<?> savePost(@RequestBody PostDto postDto)
    {
        boolean isSaved = postService.savePost(postDto);
        if (isSaved) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
