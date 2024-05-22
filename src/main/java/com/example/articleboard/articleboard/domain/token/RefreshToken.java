package com.example.articleboard.articleboard.domain.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@Entity
@Table(name = "TB_REFRESH_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    private Long id;

    private String refreshToken;

    public RefreshToken updateRefresh(String newRefresh) {
        this.refreshToken = newRefresh;
        return this;
    }
}