package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.Media;
import com.example.articleboard.articleboard.domain.Newsletter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    Page<Newsletter> findAll(Pageable pageable);
    Page<Newsletter> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Newsletter> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<Newsletter> findByTitleContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);
}
