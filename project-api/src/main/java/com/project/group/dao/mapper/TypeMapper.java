package com.project.group.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.group.dao.pojo.Type;

import java.util.List;

public interface TypeMapper extends BaseMapper<Type> {

    /**
     * 寻找最热标签
     * @param limit
     * @return
     */
    List<Type> findHostType(int limit);
}
