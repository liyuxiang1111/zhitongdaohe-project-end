package com.project.blog.controller;


import com.project.blog.service.SysUserService;
import com.project.blog.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("users")
public class UserController {
    @Resource
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token ){
        return sysUserService.findUserByToken(token);
    }

}
