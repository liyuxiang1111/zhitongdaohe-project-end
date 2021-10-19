package com.project.group.service;

import com.project.group.vo.Result;

public interface TypeService {

    Result TypeDetailById(Long id);


    /**
     * 首页最热文章
     * @param limit
     * @return
     */
    Result hotType(int limit);
}
