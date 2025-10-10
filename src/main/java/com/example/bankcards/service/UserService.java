package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.ErrorCode;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.E_USER_NOT_FOUND.name()));
        return userMapper.fromUserToUserDto(user);
    }

    public UserDto updateUserInfo(Long id, UserDto updatedUser) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.E_USER_NOT_FOUND.name()));

        if (updatedUser.email() != null && !updatedUser.email().equals(user.getEmail())) {
            if (userRepository.findByEmail(updatedUser.email()).isPresent()) {
                throw new IllegalStateException(ErrorCode.E_USERNAME_TAKEN_BY_ANOTHER_USER.name());
            }
            user.setEmail(updatedUser.email());
        }

        if (updatedUser.name() != null) {
            user.setName(updatedUser.name());
        }

        if (updatedUser.password() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.password()));
        }

        var saved = userRepository.save(user);
        return userMapper.fromUserToUserDto(saved);
    }
}
