package com.example.articleboard.articleboard.repository;

import com.example.articleboard.articleboard.domain.Gallery;
import com.example.articleboard.articleboard.domain.RoleType;
import com.example.articleboard.articleboard.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);
    Page<User> findByUsernameContaining(String searchKeyword, Pageable pageable);

    Page<User> findByRole(RoleType roleType, Pageable pageable);

    Page<User> findByRoleContainingOrUsernameContaining(String role, String searchKeyword, Pageable pageable);
}
