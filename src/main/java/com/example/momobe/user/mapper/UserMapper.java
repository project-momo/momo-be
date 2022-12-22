package com.example.momobe.user.mapper;

import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.RedisUserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RedisUserDto of(User user);
}
