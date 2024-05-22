package com.example.articleboard.articleboard.domain.dto.request;

import com.example.articleboard.articleboard.domain.RoleType;
import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;
    private String username;
    private String password;
    private RoleType role;
}
