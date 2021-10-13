package com.project.blog.controller;


import com.project.blog.service.LoginService;
import com.project.blog.vo.Result;
import com.project.blog.vo.params.LoginParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("register")
public class RegisterController {


    @Resource
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody LoginParam loginParam){
        //sso 单点登录 后期如果吧注册功能 提出去 可以提供独立接口来提供服务
        return loginService.register(loginParam);
    }

}
