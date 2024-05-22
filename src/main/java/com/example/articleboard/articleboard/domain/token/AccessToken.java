package com.example.articleboard.articleboard.domain.token;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Setter
@Entity
@Table(name = "TB_ACCESS_TOKEN")
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessTokenId;

    private String assessToken;

    private Long id;
}