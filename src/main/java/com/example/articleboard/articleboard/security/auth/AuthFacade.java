package com.example.articleboard.articleboard.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getId() {
        Authentication authentication = this.getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthDetailsImpl) {
            System.out.println("파사드 성공!");
            return ((AuthDetailsImpl) authentication.getPrincipal()).getUser().getId();
        } else {
            // 유효하지 않으면. -1로 오류임 띄워주게끔.
            System.out.println("파사드 오류!");
            Long aLong = Long.valueOf(-1);
            return aLong;
        }
    }
}
