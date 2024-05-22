package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findAll(Pageable pageable);

    Page<Video> findByProductNameContaining(String searchKeyword, Pageable pageable);

    Page<Video> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<Video> findByProductNameContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);
}
