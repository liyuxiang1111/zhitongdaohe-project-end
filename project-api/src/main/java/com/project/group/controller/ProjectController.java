package com.project.group.controller;


import com.project.group.common.aop.LogAnnotation;
import com.project.group.common.cache.Cache;
import com.project.group.dao.pojo.ProjectResource;
import com.project.group.service.ProjectService;
import com.project.group.vo.Result;
import com.project.group.vo.params.ProjectParam;
import com.project.group.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RequestMapping("project")
//json数据
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 进入搜索页面显示所有项目
     * @param pageParams
     * @return
     */
    @GetMapping("view")
    @LogAnnotation(module="项目",operator="获取项目列表")
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listProject(@RequestBody PageParams pageParams){
        return projectService.listProject(pageParams);
    }




    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return projectService.findProjectById(articleId);
    }


    /**
     * 创建项目
     * @param request
     * @return
     */
    @PostMapping("create")
    public Result createProject(HttpServletRequest request ) throws IOException {
        return projectService.createProject(request);
    }

    @PostMapping("post")
    public Result postResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return projectService.postThisResource(request,response);
    }

    @DeleteMapping("resouce")
    public Result deleteResource(@RequestBody ProjectResource projectResource) throws IOException {
        return projectService.deleteResource(projectResource);
    }

    @DeleteMapping("delete")
    public Result deleteProjectById(@RequestHeader Integer projectId){
        return projectService.deleteProjectById(projectId);
    }
}
