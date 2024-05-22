package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.request.UserUpdateDto;
import com.example.articleboard.articleboard.domain.dto.response.ResponseDto;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserRepository userRepository;

    @PutMapping("/usermanage/{id}")
    public @ResponseBody ResponseDto<?> updateUserRole(@RequestBody UserUpdateDto userUpdateDto)
    {
        adminService.updateUser(userUpdateDto);
        return new ResponseDto<>(HttpStatus.OK.value(),
                userUpdateDto.getUsername() + " 유저 권한 수정 완료");
    }

    @DeleteMapping("/usermanage/{id}")
    public @ResponseBody ResponseDto<?> deleteUser(@PathVariable long id ) {
        adminService.deleteUser(id);
        return new ResponseDto<>(HttpStatus.OK.value(), id+" 번 유저 삭제");
    }
}
