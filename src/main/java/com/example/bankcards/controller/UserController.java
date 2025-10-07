package com.example.bankcards.controller;

import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.rq.UserInfoRq;
import com.example.bankcards.rs.CurrentUserRs;
import com.example.bankcards.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@Data
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
    public ResponseEntity<CurrentUserRs> getUserProfile() {
        log.info("Called getUserProfile");
        var users = userMapper.fromUserDtoToCurrentUserRs(userService.getCurrentUser());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/profile")
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
