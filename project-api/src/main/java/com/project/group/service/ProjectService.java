package com.project.group.service;

import com.project.group.vo.Result;
import com.project.group.vo.params.ProjectParam;
import com.project.group.vo.params.PageParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ProjectService {


    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listProject(PageParams pageParams);

    /**
     * 首页文章
     * @param limit
     * @return
     */
    Result hotProject(int limit);



    /**
     * 根据id 进入对应的文章页面
     * @param articleId
     * @return
     */
    Result findProjectById(Long articleId);


    /**
     * 文章发布服务
     * @return
     */

    Result publish(HttpServletRequest request ) throws IOException;

}
