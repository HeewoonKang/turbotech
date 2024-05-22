package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Page<Media> findAll(Pageable pageable);
    Page<Media> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Media> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<Media> findByTitleContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);

}
