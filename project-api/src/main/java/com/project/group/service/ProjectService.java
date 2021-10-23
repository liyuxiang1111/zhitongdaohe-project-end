package com.project.group.service;

import com.project.group.dao.pojo.ProjectResource;
import com.project.group.vo.Result;
import com.project.group.vo.params.ProjectParam;
import com.project.group.vo.params.PageParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    Result createProject(HttpServletRequest request ) throws IOException;


    /**
     * 上传资源
     * @param request
     * @param response
     * @return
     */
    Result postThisResource(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;


    /**
     * 删除这个资源
     * @param projectResource
     * @return
     */
    Result deleteResource(ProjectResource projectResource) throws IOException;


    /**
     * 删除项目
     * @param projectId
     * @return
     */
    Result deleteProjectById(Integer projectId);
}
