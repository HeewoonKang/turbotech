package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    Page<Gallery> findAll(Pageable pageable);

    Page<Gallery> findByProductNameContaining(String searchKeyword, Pageable pageable);

    Page<Gallery> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<Gallery> findByProductNameContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);
}
