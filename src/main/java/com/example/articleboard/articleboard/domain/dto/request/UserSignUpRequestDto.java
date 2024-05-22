package com.example.articleboard.articleboard.domain.dto.request;

import com.example.articleboard.articleboard.domain.Region;
import com.example.articleboard.articleboard.domain.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserSignUpRequestDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_]{2,}$", message = "아이디는 알파벳, 숫자, _만 포함하고 최소 2자 이상이어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{10,}$", message = "비밀번호는 숫자, 특수 문자 및 영어를 포함해야 합니다.")
    private String password;

    private String familyName;
    private String name;
    private String nickName;
    private String birth;

    @Pattern(regexp="^010-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000의 형식으로 입력해야 합니다.")
    private String contact;

    private String etcContact;
    private String region;
    private String role;
    private String email;
    private String postcode;
    private String county;
    private String city;
    private String address;
    private String etcAddress;
    private Long serialNumber;
}
