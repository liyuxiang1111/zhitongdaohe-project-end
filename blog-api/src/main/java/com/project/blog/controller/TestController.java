package com.project.blog.controller;

import com.project.blog.dao.pojo.SysUser;
import com.project.blog.utils.UserThreadLocal;
import com.project.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
