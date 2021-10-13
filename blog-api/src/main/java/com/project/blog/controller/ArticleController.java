package com.project.blog.controller;


import com.project.blog.common.aop.LogAnnotation;
import com.project.blog.common.cache.Cache;
import com.project.blog.service.ArticleService;
import com.project.blog.vo.Result;
import com.project.blog.vo.params.ArticleParam;
import com.project.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("projects")
//json数据
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 首页文章列表
     * @param pageParams
     * @return
     */

    @PostMapping
    @LogAnnotation(module="项目",operator="获取项目列表")
    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     * 最热项目
     * @return
     */
    @PostMapping("hot")
//    使用自定义注解 并且给定参数
    @Cache(expire = 5 * 60 * 1000, name = "hot_projects")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }
    

    /**
     * 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
