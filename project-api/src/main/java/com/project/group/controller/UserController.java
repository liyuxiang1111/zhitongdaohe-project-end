package com.project.group.controller;


import com.project.group.service.LoginService;
import com.project.group.service.SysUserService;
import com.project.group.vo.Result;
import com.project.group.vo.params.LoginParam;
import com.project.group.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @Autowired
    private LoginService loginService;



    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token ){
        return sysUserService.findUserByToken(token);
    }


    @DeleteMapping("login")
    public Result logout(@RequestHeader("Authorization") String token){
        //登录 用户访问用户表  但是
        return loginService.logout(token);
    }

    @GetMapping("/login")
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterParam registerParam){
        //sso 单点登录 后期如果吧注册功能 提出去 可以提供独立接口来提供服务
        return loginService.register(registerParam);
    }

    @PostMapping("change")
    public Result changeUser(HttpServletRequest request){
        return sysUserService.changeUserInformation(request);
    }

    @DeleteMapping("delete")
    public Result deleteUser(@RequestHeader("Authorization") String token){
        return sysUserService.deleteUser(token);
    }



}
