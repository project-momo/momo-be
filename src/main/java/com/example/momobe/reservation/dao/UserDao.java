package com.example.momobe.reservation.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {
    @Select("SELECT user_point FROM user WHERE user_id = #{userId}")
    Long selectUserPointById(Long userId);

    @Update("UPDATE user SET user_point = user_point + #{pointValue} " +
            "WHERE user_id = #{userId}")
    void updateUserPoint(@Param("userId") Long userId, @Param("userPoint") Long userPoint);
}
