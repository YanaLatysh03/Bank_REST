package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.rq.UserInfoRq;
import com.example.bankcards.rs.CurrentUserRs;
import com.example.bankcards.rs.UserInfoRs;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto fromUserToUserDto(User user);
    CurrentUserRs fromUserDtoToCurrentUserRs(UserDto user);
    UserInfoRs fromUserDtoToUserInfoRs(UserDto user);
    UserDto fromUserInfoRqToUserDto(UserInfoRq user);
}
