package com.example.bankcards.controller;

import com.example.bankcards.entity.Role;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.rs.UserInfoRs;
import com.example.bankcards.service.AdminService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@Data
public class AdminController {
    private final AdminService adminService;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(AdminController.class);

    @GetMapping()
    public ResponseEntity<List<UserInfoRs>> getAllUsers() {
        log.info("Called getAllUsers");
        var users = adminService.getAllUsers()
                .stream().map(userMapper::fromUserDtoToUserInfoRs).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ) {
        log.info("Called deleteReservation, id = " + id);
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<UserInfoRs> updateUserRole(
            @PathVariable("id") Long id,
            @RequestBody Role role
    ) {
        var updatedUser = userMapper.fromUserDtoToUserInfoRs(
                adminService.updateRole(id, role)
        );

        return ResponseEntity.ok(updatedUser);
    }
}
