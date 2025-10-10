package com.example.bankcards.service;

import com.example.bankcards.entity.ErrorCode;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.rq.RegisterRq;
import com.example.bankcards.rs.AuthResponse;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse register(RegisterRq request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException(ErrorCode.E_USERNAME_TAKEN_BY_ANOTHER_USER.name());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setRole(Role.ROLE_USER);
        var createUser = userRepository.save(user);

        var userDetails = userDetailsService.loadUserByUsername(createUser.getEmail());
        var token = jwtService.generateToken(userDetails);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(createUser.getEmail());
        response.setRole(createUser.getRole().name());
        return response;
    }

    public AuthResponse login(String email, String password) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(userDetails.getUsername());
        response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
        return response;
    }
}
