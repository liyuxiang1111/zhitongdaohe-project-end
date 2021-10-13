package com.project.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.blog.dao.pojo.Tag;

import java.util.List;


public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id来查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagByArticleId(Long articleId);

    /**
     * 查询最热的标签 前limit条
     * @param limit
     * @return
     */

    List<Long> findHostsTagIds(int limit);

    /**
     * 按id 查询
     * @param tagIds
     * @return
     */
    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
