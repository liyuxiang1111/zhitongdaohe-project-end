package com.project.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.blog.dao.mapper.CategoryMapper;
import com.project.blog.dao.pojo.Category;
import com.project.blog.service.CategoryService;
import com.project.blog.vo.CategoryVo;
import com.project.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;


    @Override
    public CategoryVo findArticleCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        减少查询的次数
        queryWrapper.select(Category::getId,Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        // 和页面交互的vo
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);

        // 和页面交互的vo
        return Result.success(copyList(categories));
    }


    @Override
    public Result categoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(String.valueOf(category.getId()));
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

}
