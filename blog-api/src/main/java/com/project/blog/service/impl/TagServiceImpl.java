package com.project.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.blog.dao.mapper.TagMapper;
import com.project.blog.dao.pojo.Tag;
import com.project.blog.service.TagService;
import com.project.blog.vo.Result;
import com.project.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;


    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    @Override
    public List<TagVo> findTagByArticleId(Long articleId) {
        //mybaties-plus 无法进行夺标查询
        List<Tag> tags = tagMapper.findTagByArticleId(articleId);

        return copyList(tags);
    }



    @Override
    public Result host(int limit) {
        /**
         * 1.标签所拥有的文章数量最多就是最热标签
         * 2. 查询通过tag_id 分组技术 重大到小排序
         *
         */
        List<Long> tagIds =  tagMapper.findHostsTagIds(limit);
        //判断id集合是否为空
        if(CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        //需求是 tagid 何 tagName  tag对象
        // select * from tag where id in (1,2,3,4)
        List<Tag> tagList =  tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags =  this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags =  this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(tags));
    }


    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        tagVo.setId(String.valueOf(tag.getId()));
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }

}
