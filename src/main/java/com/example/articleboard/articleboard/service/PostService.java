package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.Post;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.PostDto;
import com.example.articleboard.articleboard.domain.dto.PostEditDto;
import com.example.articleboard.articleboard.exception.custom.PostCouldNotBePosted;
import com.example.articleboard.articleboard.exception.custom.UserNotFoundException;
import com.example.articleboard.articleboard.security.jwt.JwtProvider;
import com.example.articleboard.articleboard.repository.PostRepository;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.security.auth.AuthFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = false)
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthFacade authFacade;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public long countPost() {
        return postRepository.count();
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id)
    {
        return postRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostList(Pageable pageable)
    {
        return postRepository.findAll(pageable);
    }


    public boolean updatePost(Long id, PostEditDto postEditDto) {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(UserNotFoundException::new);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if (!post.getUser().getId().equals(findUser.getId())) {
            throw new IllegalArgumentException("게시글의 작성자만 수정할 수 있습니다.");
        }

        post.setTitle(postEditDto.getTitle());
        post.setContent(postEditDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        log.info("updatePost >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("updatePost >>> postInfo: {}", post);

        Long postId = postRepository.save(post).getId();
        if(postId > 0) return true;
        else throw new IllegalArgumentException("게시글 수정에 실패했습니다.");
    }
    public boolean savePost(PostDto postDto)
    {
        User findUser = userRepository.findById(authFacade.getId()).orElseThrow(UserNotFoundException::new);

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(findUser);

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        log.info("savePost >>> LocalDateTime.now(): {}", LocalDateTime.now());
        log.info("savePost >>> postInfo: {}", post);

        Long postId = postRepository.save(post).getId();
        if(postId > 0) return true;
        else throw new PostCouldNotBePosted();
    }
}
