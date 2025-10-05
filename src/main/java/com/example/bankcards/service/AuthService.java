package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Заменить на свои исключения
            throw new UsernameNotFoundException("Пользователь с таким именем уже существует");
        }

        return userRepository.save(user);
    }
}
