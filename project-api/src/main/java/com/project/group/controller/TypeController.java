package com.project.group.controller;


import com.project.group.service.TypeService;
import com.project.group.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 项目类型
 */

@RestController
@RequestMapping("type")
public class TypeController {

    @Resource
    private TypeService typeService;


    /**
     * 具体的进入一个标签
     * @param id
     * @return
     */

    @PostMapping("{id}")
    public Result TypeDetailById(@PathVariable("id") Long id){
        return typeService.TypeDetailById(id);
    }




}
