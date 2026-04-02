package com.Mike12138210.mapper;

import com.Mike12138210.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    // 插入用户（注册）
    @Insert("INSERT INTO user (account, password, role, building, room) VALUES (#{account}, #{password}, #{role}, #{building}, #{room})")
    int insertUser(User user);

    // 删除用户信息
    void deleteUser();

    // 修改宿舍信息
    @Update("UPDATE user SET building = #{building},room = #{room} WHERE account = #{account}")
    int updateDorm(@Param("account")String account,
                   @Param("building")String building,
                   @Param("room")String room);

    // 根据账号查询用户（用于登录）
    @Select("SELECT id,account,password,role,building,room FROM user WHERE account = #{account} ")
    User selectByAccount(String account);

    // 根据账号获得Id
    @Select("SELECT id FROM user WHERE account = #{account}")
    Integer getIdByAccount(String account);

    // 修改密码
    @Update("UPDATE user SET password = #{newPwd} WHERE id = #{id}")
    int updatePwd(@Param("newPwd") String newPwd, @Param("id") Integer id);
}