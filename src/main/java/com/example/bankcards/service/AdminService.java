package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data
public class AdminService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::fromUserToUserDto).toList();
    }

    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return userMapper.fromUserToUserDto(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        userRepository.delete(user);
    }

    public UserDto updateRole(Long id, Role role) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        user.setRole(Role.ADMIN);
        var updatedUser = userRepository.save(user);
        return userMapper.fromUserToUserDto(updatedUser);
    }
}
