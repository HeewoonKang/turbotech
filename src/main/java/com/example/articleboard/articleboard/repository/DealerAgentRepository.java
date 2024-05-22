package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.DealerAgent;
import com.example.articleboard.articleboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerAgentRepository extends JpaRepository<DealerAgent, Long> {
    Page<DealerAgent> findAll(Pageable pageable);

    Page<DealerAgent> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<DealerAgent> findByUser_NickNameContaining(String nickName, Pageable pageable);

    Page<DealerAgent> findByTitleContainingOrUser_NickNameContaining(String title, String nickName, Pageable pageable);
}
