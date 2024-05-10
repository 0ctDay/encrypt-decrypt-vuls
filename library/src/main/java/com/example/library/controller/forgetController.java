package com.example.library.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.library.commom.Result;
import com.example.library.entity.User;
import com.example.library.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/forget")
public class forgetController {
    @Resource
    UserMapper userMapper;
    
    @PostMapping("/checkusername")
    public Result<?> checkusername(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res == null)
        {
            return Result.error("-1","用户名不存在");
        }
        return Result.success("0");
    }
   
    @PostMapping("/sendsms")
    public Result<?> sendsms(@RequestBody User user, HttpServletRequest request){

        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone,user.getPhone()).eq(User::getUsername,user.getUsername()));
        if(res == null)
        {
            return Result.error("-1","手机号错误");
        }
        HttpSession session = request.getSession();
        session.setAttribute("code",1234);
        System.out.println(session.getAttribute("code"));
        return Result.success("0");
    }
   
    @GetMapping("/checksms")
    public Result<?> checksms(HttpServletRequest request){
        HttpSession session = request.getSession();
        String code = (String)session.getAttribute("code");
        System.out.println(code);

        try {
            if ("1234".equals(request.getParameter("code"))) {
                return Result.success("0");
            } else {
                return Result.error("-1", "验证码错误");
            }
        }
        catch (Exception e) {
            return Result.error("-1", "验证码错误");
        }

    }
   
    @PostMapping("/resetpassword")
    public Result<?> resetpassword(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        //User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPhone,user.getPhone()));
        if(res == null)
        {
            return Result.error("-1","用户名或手机号错误");
        }
        res.setPassword(user.getPassword());
        userMapper.updateById(res);
        return Result.success();
    }
}
