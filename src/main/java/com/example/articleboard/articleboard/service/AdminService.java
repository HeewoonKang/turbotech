package com.example.articleboard.articleboard.service;

import com.example.articleboard.articleboard.domain.User;
import com.example.articleboard.articleboard.domain.dto.request.UserUpdateDto;
import com.example.articleboard.articleboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        // 검색결과가 없을 때 빈 User 객체로 반환
        User findUser = userRepository.findById(id).orElseGet(
                new Supplier<User>() {
                    @Override
                    public User get() {
                        return new User();
                    }
                });
        return findUser;
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(UserUpdateDto userUpdateDto)
    {
        User findUser = userRepository.findById(userUpdateDto.getId()).get();
        findUser.setUsername(userUpdateDto.getUsername());
        findUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        findUser.setRole(userUpdateDto.getRole());

        userRepository.save(findUser);
    }
}
