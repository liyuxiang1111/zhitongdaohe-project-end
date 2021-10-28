package com.project.group.controller;


import com.project.group.common.cache.Cache;
import com.project.group.service.ProjectService;
import com.project.group.service.TypeService;
import com.project.group.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("index")
public class IndexController {


    @Resource
    private ProjectService projectService;

    @Resource
    private TypeService typeService;


    /**
     * 最热项目
     * @return
     */
    @GetMapping("hot/project")
//    使用自定义注解 并且给定参数
    @Cache(expire = 5 * 60 * 1000, name = "hot_projects")
    public Result hotArticle(){
        int limit = 8;
        return projectService.hotProject(limit);
    }


    @GetMapping("hot/type")
    //@Cache(expire = 5 * 60 * 1000, name = "hot_type")
    public Result hotTypes(){
        int limit = 6;
        return typeService.hotType(limit);
    }

}
