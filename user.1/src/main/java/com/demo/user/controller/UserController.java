package com.demo.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.user.form.UserForm;
import com.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: rosh
 * @Date: 2021/10/25 22:11
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public UserForm login(@RequestBody UserForm userForm) {

        return userForm;
    }

    @GetMapping("/detail")
    public JSONObject detail(@RequestParam("id") Long id) {

        return userService.detail(id);
    }
}
