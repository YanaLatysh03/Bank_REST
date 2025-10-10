package com.example.bankcards.controller;

import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.rq.UserInfoRq;
import com.example.bankcards.rs.CurrentUserRs;
import com.example.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@Data
@Tag(name = "Профиль пользователя")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
    @Operation(summary = "Получение профиля пользователя")
    public ResponseEntity<CurrentUserRs> getUserProfile() {
        log.info("Called getUserProfile");
        var users = userMapper.fromUserDtoToCurrentUserRs(userService.getCurrentUser());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/profile")
    @Operation(summary = "Изменение профиля пользователя")
    public ResponseEntity<CurrentUserRs> updateUserProfile(
            @RequestBody UserInfoRq user
    ) {
        log.info("Called updateUserProfile: " + "/n user = " + user);
        var currentUser = userService.getCurrentUser();

        var updatedUser = userMapper.fromUserDtoToCurrentUserRs(
                userService.updateUserInfo(currentUser.id(), userMapper.fromUserInfoRqToUserDto(user))
        );
        return ResponseEntity.ok(updatedUser);
    }
}
