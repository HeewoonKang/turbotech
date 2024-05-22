package com.example.articleboard.articleboard.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Gallery")
public class Gallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String productName;

    @Lob
    @Column(nullable = false)
    private String description;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_path")
    private String imagePath;

    @Column(nullable = false)
    private String copyright;

    @Column(nullable = false)
    private String Resolution;

    @Column
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    private User user;
}
