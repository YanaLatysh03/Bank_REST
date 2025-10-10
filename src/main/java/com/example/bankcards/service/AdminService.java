package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.ErrorCode;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.search.AdminUserSearchFilter;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Data
public class AdminService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(AdminService.class);

    public List<UserDto> getAllUsersByFilter(AdminUserSearchFilter filter) {
        log.info("Called getAllUsersByFilter");

        int pageSize = filter.pageSize() != null
                ? filter.pageSize() : 10;

        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        var users = userRepository.findAllByFilter(filter.name(), filter.email(), pageable);
        return users.stream().map(userMapper::fromUserToUserDto).toList();
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.E_USER_NOT_FOUND.name()));
    }

    public void deleteUser(Long id) {
        var user = getUserById(id);

        userRepository.delete(user);
    }

    public UserDto updateRole(Long id, Role role) {
        var user = getUserById(id);

        user.setRole(Role.ROLE_ADMIN);
        var updatedUser = userRepository.save(user);
        return userMapper.fromUserToUserDto(updatedUser);
    }
}
