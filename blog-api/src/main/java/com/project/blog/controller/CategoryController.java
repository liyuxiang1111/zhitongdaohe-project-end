package com.project.blog.controller;


import com.project.blog.service.CategoryService;
import com.project.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public Result categoryies(){
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }


}
