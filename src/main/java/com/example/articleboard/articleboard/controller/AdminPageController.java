package com.example.articleboard.articleboard.controller;

import com.example.articleboard.articleboard.domain.Post;
import com.example.articleboard.articleboard.domain.RoleType;
import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.repository.UserRepository;
import com.example.articleboard.articleboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminPageController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/membership/Admin")
    public String AdminPage(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "5") Integer size)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> userList = userService.getUserList(pageable);
        long count = userService.countUser();
        model.addAttribute("totalUsers", count);
        model.addAttribute("users", userList);

        return "th/Turbo/turboAdmin";
    }

    /*@GetMapping("/membership/Admin/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam("category") String category,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> userList;
        if ("role".equals(category)) {
            RoleType roleType = convertStringToRoleType(keyword);
            if (roleType != null) {
                userList = userRepository.findByRole(roleType, pageable);
            } else {
                userList = Page.empty(pageable);
            }
        } else if ("username".equals(category)) {
            userList = userRepository.findByUsernameContaining(keyword, pageable);
        } else if ("all".equals(category)) {
            // RoleType roleType = convertStringToRoleType(keyword);
            userList = userRepository.findByRoleContainingOrUsernameContaining(roleType, keyword, pageable);
        } else {
            userList = userRepository.findAll(pageable);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("content", userList.getContent());
        response.put("last", userList.isLast());
        response.put("totalUsers", userList.getTotalElements());

        return ResponseEntity.ok(response);
    }*/
    @GetMapping("/membership/Admin/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam("category") String category,
                                                           @RequestParam("keyword") String keyword,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "5") Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> userList;

        if ("role".equals(category)) {
            RoleType roleType = convertStringToRoleType1(keyword);
            userList = roleType != null ? userRepository.findByRole(roleType, pageable) : Page.empty(pageable);
        } else if ("username".equals(category)) {
            userList = userRepository.findByUsernameContaining(keyword, pageable);
        } else if ("all".equals(category)) {
            userList = AllCategorySearch(keyword, pageable);
        } else {
            userList = userRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", userList.getContent());
        response.put("last", userList.isLast());
        response.put("totalUsers", userList.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // 검색어 한글화
    private RoleType convertStringToRoleType1(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        switch (keyword) {
            case "일반":
                return RoleType.GENERAL;
            case "대리점":
                return RoleType.RETAIL;
            case "관리자":
                return RoleType.ADMIN;
            case "유저":
                return RoleType.USER;
            default:
                return null;
        }
    }

    public RoleType convertStringToRoleType(String roleStr) {
        try {
            return RoleType.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private Page<User> AllCategorySearch(String keyword, Pageable pageable) {
        RoleType roleType = convertStringToRoleType1(keyword);
        if (roleType != null) {
            // RoleType의 검색어(한글)
            return userRepository.findByRole(roleType, pageable);
        } else {
            // 유저명 검색
            return userRepository.findByUsernameContaining(keyword, pageable);
        }
    }

}
