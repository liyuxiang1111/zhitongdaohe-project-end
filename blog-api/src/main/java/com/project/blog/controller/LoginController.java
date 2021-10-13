package com.project.blog.controller;


import com.project.blog.service.LoginService;
import com.project.blog.vo.Result;
import com.project.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {


//    @Autowired
//    private SysUserService sysUserService;
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登录 用户访问用户表  但是
        return loginService.login(loginParam);
    }
}
