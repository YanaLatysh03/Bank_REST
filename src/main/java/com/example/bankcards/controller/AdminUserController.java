package com.example.bankcards.controller;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.SuccessCode;
import com.example.bankcards.filter.AdminUserSearchFilter;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.rs.SuccessfulRs;
import com.example.bankcards.rs.UserInfoRs;
import com.example.bankcards.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/admin/users")
@Data
@Tag(name = "Управление пользователями (администратор)")
public class AdminUserController {
    private final AdminService adminService;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @GetMapping("/")
    @Operation(summary = "Получение всех пользователей")
    public ResponseEntity<List<UserInfoRs>> getAllUsers(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        log.info("Called getAllUsers");

        var filter = new AdminUserSearchFilter(
                name,
                email,
                pageSize,
                pageNumber
        );

        var users = adminService.getAllUsersByFilter(filter)
                .stream().map(userMapper::fromUserDtoToUserInfoRs).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя")
    public ResponseEntity<SuccessfulRs> deleteUser(
            @PathVariable("id") Long id
    ) {
        log.info("Called deleteReservation, id = " + id);
        adminService.deleteUser(id);
        return ResponseEntity.ok(new SuccessfulRs(SuccessCode.USER_DELETED.name()));
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Изменение роли пользователя")
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
