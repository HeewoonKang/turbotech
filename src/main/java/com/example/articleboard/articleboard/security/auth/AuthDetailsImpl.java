package com.example.articleboard.articleboard.security.auth;

import com.example.articleboard.articleboard.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
public class AuthDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        // 권한 목록
        Collection<GrantedAuthority> roleList = new ArrayList<>();

        // 권한 목록 설정
        roleList.add(new GrantedAuthority()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String getAuthority()
            {
                return "ROLE_" + user.getRole();
            }
        });

        return roleList;
    }
    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
