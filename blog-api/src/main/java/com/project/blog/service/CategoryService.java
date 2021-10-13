package com.project.blog.service;

import com.project.blog.vo.CategoryVo;
import com.project.blog.vo.Result;

public interface CategoryService {

    CategoryVo findArticleCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
