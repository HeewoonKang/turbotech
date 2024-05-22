package com.example.articleboard.articleboard.security.auth;

import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.exception.custom.UserNotFoundException;
import com.example.articleboard.articleboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AuthDetailsImpl loadUserByUsername(String id) throws UsernameNotFoundException {
        log.info("In AuthDetailService, loadUserByUsername id is :"+id);
        User user = userRepository.findById(Long.parseLong(id))
                .orElseThrow(UserNotFoundException::new);
        return userRepository.findById(Long.parseLong(id))
                .map(AuthDetailsImpl::new)
                .orElseThrow(UserNotFoundException::new);
    }
}
