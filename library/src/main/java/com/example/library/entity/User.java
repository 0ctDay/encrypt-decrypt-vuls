package com.example.library.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
@TableName("user")
@Data
public class User {
    @TableId (type = IdType.AUTO)
    private Integer id;
    private String username;
    private String nickName;

    @JsonIgnore
    private String password;

    private String sex;
    private String address;
    private String phone;
    @TableField(exist = false)  //表中没有token不会报错仍能编译运行
    private String token;
    private Integer role;

}
