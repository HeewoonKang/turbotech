package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Post> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<Post> findByTitleContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);

}
