package com.example.momobe.user.mapper;

import com.example.momobe.user.domain.User;
import com.example.momobe.user.dto.RedisUserDto;
import com.example.momobe.user.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    RedisUserDto of(User user);

//    @Mapping(target = "nickname", source = "nickname.nickname")
//    @Mapping(target = "email", source = "email.address")
//    @Mapping(target = "point", source = "userPoint.point")
//    UserResponseDto userDtoOfUser(User user);

    default UserResponseDto userDtoOfUser(User user){
        return UserResponseDto.builder()
                .nickname(user.getNickname().getNickname())
                .email(user.getEmail().getAddress())
                .point(user.getUserPoint().getPoint())
                .avatar(user.getAvatar().getRemotePath())
                .build();
    }
}
