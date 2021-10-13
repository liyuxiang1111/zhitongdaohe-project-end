package com.project.blog.controller;


import com.project.blog.service.CommentsService;
import com.project.blog.vo.Result;
import com.project.blog.vo.params.CommentParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("comments")
public class CommentController {


    @Resource
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByarticleId(id);
    }


    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}
