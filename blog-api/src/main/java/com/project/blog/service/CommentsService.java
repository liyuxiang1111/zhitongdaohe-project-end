package com.project.blog.service;

import com.project.blog.vo.Result;
import com.project.blog.vo.params.CommentParam;

public interface CommentsService {
    /**
     * 查看文章评论列表
     * @param id
     * @return
     */
    Result commentsByarticleId(Long id);

    Result comment(CommentParam commentParam);
}
