package com.project.blog.service;

import com.project.blog.vo.Result;
import com.project.blog.vo.params.ArticleParam;
import com.project.blog.vo.params.PageParams;

public interface ArticleService {


    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 首页最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);



    /**
     * 根据id 进入对应的文章页面
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);


    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */

    Result publish(ArticleParam articleParam);

    Result listArchives();
}
