package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.BaseMockServiceTest;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest extends BaseMockServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testUpdateUserInfoWithEmailTakenByAnotherUser() {
        // preconditions
        var updatedUser = new UserDto("test@test.com");
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        var randomUser = new User();
        randomUser.setEmail("test@test.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(randomUser));

        // when and then
        assertThrows(IllegalStateException.class, () -> userService.updateUserInfo(1L, updatedUser));
    }

    @Test
    public void testUpdateUserInfo() {
        // preconditions
        var newUserInfo = new UserDto(1L, "test@test.com", "test", "test", Role.ROLE_USER);
        var user = new User();
        user.setId(1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenAnswer(returnsFirstArg());

        // when
        var updatedUser = userService.updateUserInfo(1L, newUserInfo);

        // verify
        verify(userRepository, times(1)).save(any());

        // then
        Assertions.assertEquals(newUserInfo.id(), updatedUser.id());
        Assertions.assertEquals(newUserInfo.email(), updatedUser.email());
        Assertions.assertEquals(newUserInfo.name(), updatedUser.name());
        Assertions.assertTrue(passwordEncoder.matches(newUserInfo.password(), updatedUser.password()));
    }
}
