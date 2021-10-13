package com.project.blog.service;

import com.project.blog.vo.Result;
import com.project.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagByArticleId(Long articleId);

    Result host(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    /**
     * 按名字显示对应的tag
     * @return
     */
    Result findDetailById(Long id);
}
