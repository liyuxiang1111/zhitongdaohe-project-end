package com.project.blog.controller;


import com.project.blog.service.LoginService;
import com.project.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {


    //    @Autowired
//    private SysUserService sysUserService;
    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        //登录 用户访问用户表  但是
        return loginService.logout(token);
    }
}
