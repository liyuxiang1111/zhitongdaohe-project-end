package com.project.blog.controller;


import com.project.blog.service.TagService;
import com.project.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Resource
    private TagService tagService;
    // /tags//hot
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.host(limit);
    }

    @GetMapping
    public Result findAll(){
        return tagService.findAll();
    }


    @GetMapping("detail")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }


}
