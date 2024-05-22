package com.example.articleboard.articleboard.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Entity
@NoArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;//na아이디

    @Column(nullable = false)
    private String password;//비밀번호

    @Column(nullable = false)
    @ColumnDefault("'DefaultFamilyName'")
    private String familyName;  // 이름(성)

    @Column(nullable = false, length = 30)
    private String name;    //이름(실명)


    @Column(nullable = false)
    private String birth;   // 생년월일

    @Column(nullable = false, length = 30)
    private String nickName;    //별명

    @Column
    private String email;

    @Column(nullable = false)
    @Pattern(regexp="^010-\\d{4}-\\d{4}$")
    @ColumnDefault("'010-0000-0000'")
    private String contact; // 연락처

    @Column
    private String etcContact;  // 기타 연락처

    @Column(nullable = false)
    @ColumnDefault("'KR'")
    private String region;  // 국가

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column
    @ColumnDefault("'00000'")
    private String postcode;  // 우편번호

    @Column
    private String county;    // 주/도/지역

    @Column
    private String city;    // 도시

    @Column
    private String address; // 거리 및 번호, 사서함

    @Column
    private String etcAddress;  // 아파트, 방, 호수, 빌딩 등

    @Column
    private Long serialNumber;  // 시리얼 넘버

    public User(String username, String password, String name, String nickName) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        // this.role = RoleType.USER;
    }
}