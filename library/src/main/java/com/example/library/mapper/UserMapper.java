package com.example.library.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.library.entity.User;

public interface UserMapper extends BaseMapper<User> {
//    @Insert("INSERT INTO user_table (username, password, email) VALUES (#{username}, #{password}, #{email})")
//    void insertUser(User user);

}
