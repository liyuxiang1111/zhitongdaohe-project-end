package com.project.group.service;


import com.project.group.vo.Result;
import org.springframework.stereotype.Service;

public interface MemberService {

    /**
     * 进入成员页面
     * @param projectId
     * @return
     */
    Result memberPage(Integer projectId);
}
