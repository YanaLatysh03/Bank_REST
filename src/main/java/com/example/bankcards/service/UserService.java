package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
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
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return userMapper.fromUserToUserDto(user);
    }

    public UserDto updateUserInfo(Long id, UserDto updatedUser) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        if (updatedUser.username() != null && !updatedUser.username().equals(user.getUsername())) {
            if (userRepository.findByUsername(updatedUser.username()).isPresent()) {
                throw new UsernameNotFoundException("Пользователь с таким именем уже существует");
            }
            user.setUsername(updatedUser.username());
        }

        if (updatedUser.password() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.password()));
        }

        var saved = userRepository.save(user);
        return userMapper.fromUserToUserDto(saved);
    }
}
